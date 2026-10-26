package com.TempoLux.application.service.impl;

import com.TempoLux.crosscutting.exception.BusinessException;
import com.TempoLux.crosscutting.exception.InsufficientStockException;
import com.TempoLux.crosscutting.exception.ResourceNotFoundException;
import com.TempoLux.crosscutting.utils.OrderCodeGenerator;
import com.TempoLux.domain.dto.request.OrderCreateRequestDto;
import com.TempoLux.domain.dto.request.OrderItemRequestDto;
import com.TempoLux.domain.dto.response.OrderResponseDto;
import com.TempoLux.domain.dto.response.OrderStatusResponseDto;
import com.TempoLux.domain.enums.OrderStatus;
import com.TempoLux.domain.mappers.OrderDtoMapper;
import com.TempoLux.domain.model.Order;
import com.TempoLux.domain.model.OrderItem;
import com.TempoLux.domain.model.User;
import com.TempoLux.domain.port.OrderRepository;
import com.TempoLux.application.service.OrderService;
import com.TempoLux.domain.port.ProductRepository;
import com.TempoLux.domain.port.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de uso de pedidos: registro y confirmación (con descuento de stock en una
 * transacción reactiva) y seguimiento por código único de orden.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Mono<OrderResponseDto> createOrder(String customerEmail, OrderCreateRequestDto request) {
        if (request == null || request.items() == null || request.items().isEmpty()) {
            return Mono.error(new BusinessException("El pedido debe contener al menos un ítem."));
        }

        String email = normalizeEmail(StringUtils.hasText(customerEmail) ? customerEmail : request.customerEmail());
        if (!StringUtils.hasText(email)) {
            return Mono.error(new BusinessException("Se requiere un correo de contacto para registrar el pedido."));
        }

        return userRepository.findByEmail(email)
                .defaultIfEmpty(User.builder().name(request.customerName()).email(email).build())
                .flatMap(user -> resolveOrderItems(request.items())
                        .flatMap(orderItems -> persistOrder(user, email, orderItems))
                        .map(context -> OrderDtoMapper.toResponse(context.order(), context.items())));
    }

    @Override
    public Mono<OrderStatusResponseDto> getOrderStatus(String orderId) {
        if (!StringUtils.hasText(orderId)) {
            return Mono.error(new BusinessException("El order_id es obligatorio."));
        }
        String code = orderId.trim().toUpperCase();
        return orderRepository.findByOrderCode(code)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Pedido no encontrado con código: " + code)))
                .flatMap(order -> orderRepository.findItemsByOrderId(order.getId())
                        .collectList()
                        .map(items -> OrderDtoMapper.toStatusResponse(order, items)));
    }

    /**
     * Valida contra el catálogo y construye las líneas del pedido con precios reales.
     */
    private Mono<List<OrderItem>> resolveOrderItems(List<OrderItemRequestDto> requested) {
        return Flux.fromIterable(requested)
                .concatMap(item -> productRepository.findById(item.productId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                                "Producto no encontrado con id: " + item.productId())))
                        .filter(product -> product.getStock() != null && product.getStock() >= item.quantity())
                        .switchIfEmpty(Mono.error(new InsufficientStockException(item.productId())))
                        .map(product -> OrderItem.builder()
                                .productId(product.getId())
                                .productName(product.getName())
                                .quantity(item.quantity())
                                .unitPrice(product.getPrice())
                                .subtotal(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())))
                                .build()))
                .collectList();
    }

    private Mono<OrderContext> persistOrder(User user, String email, List<OrderItem> orderItems) {
        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return generateUniqueCode()

                .flatMap(code -> {
                    Order order = Order.builder()
                            .orderCode(code)
                            .userId(user.getId())
                            .customerName(StringUtils.hasText(user.getName())
                                    ? user.getName()
                                    : "Cliente TempoLux")
                            .customerEmail(StringUtils.hasText(user.getEmail()) ? user.getEmail() : email)
                            .status(OrderStatus.PENDIENTE)
                            .total(total)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> orderRepository.saveOrderItems(orderItems, savedOrder.getId())
                                    .collectList()
                                    .flatMap(savedItems -> Flux.fromIterable(savedItems)
                                            .concatMap(item -> productRepository.decrementStock(item.getProductId(), item.getQuantity())
                                                    .flatMap(rowsAffected -> rowsAffected == 1
                                                            ? Mono.just(item)
                                                            : Mono.error(new InsufficientStockException(item.getProductId()))))
                                            .then()
                                            .thenReturn(new OrderContext(savedOrder, savedItems))));
                });
    }

    private Mono<String> generateUniqueCode() {
        return Mono.defer(() -> {
            String code = OrderCodeGenerator.generate();
            return orderRepository.findByOrderCode(code)
                    .hasElement()
                    .flatMap(exists -> Boolean.TRUE.equals(exists)
                            ? generateUniqueCode()
                            : Mono.just(code));
        });
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private record OrderContext(Order order, List<OrderItem> items) {
    }
}
