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
import com.TempoLux.domain.model.Product;
import com.TempoLux.domain.model.User;
import com.TempoLux.domain.port.OrderRepository;
import com.TempoLux.domain.port.ProductRepository;
import com.TempoLux.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, productRepository, userRepository);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        String customerEmail = "juan@test.com";
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                "Juan Pérez",
                customerEmail,
                List.of(new OrderItemRequestDto(10L, 2, new BigDecimal("8500.00")))
        );

        User user = User.builder().id(1L).name("Juan Pérez").email(customerEmail).build();
        Product product = Product.builder().id(10L).name("Rolex Submariner").price(new BigDecimal("8500.00")).stock(5).build();
        Order savedOrder = Order.builder()
                .id(100L)
                .orderCode("ORD-2026-ABC")
                .userId(1L)
                .customerName("Juan Pérez")
                .customerEmail(customerEmail)
                .status(OrderStatus.PENDIENTE)
                .total(new BigDecimal("17000.00"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        OrderItem savedItem = OrderItem.builder()
                .id(1L)
                .orderId(100L)
                .productId(10L)
                .productName("Rolex Submariner")
                .quantity(2)
                .unitPrice(new BigDecimal("8500.00"))
                .subtotal(new BigDecimal("17000.00"))
                .build();

        when(userRepository.findByEmail(customerEmail)).thenReturn(Mono.just(user));
        when(productRepository.findById(10L)).thenReturn(Mono.just(product));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(savedOrder));
        when(orderRepository.saveOrderItems(anyList(), eq(100L))).thenReturn(Flux.just(savedItem));
        when(productRepository.decrementStock(10L, 2)).thenReturn(Mono.just(1));
        // Mock findByOrderCode for generateUniqueCode
        when(orderRepository.findByOrderCode(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.createOrder(customerEmail, request))
                .expectNextMatches(dto -> {
                    assertThat(dto.orderId()).startsWith("ORD-");
                    assertThat(dto.customerName()).isEqualTo("Juan Pérez");
                    assertThat(dto.total()).isEqualTo(new BigDecimal("17000.00"));
                    assertThat(dto.items()).hasSize(1);
                    return true;
                })
                .verifyComplete();

        verify(userRepository).findByEmail(customerEmail);
        verify(productRepository).findById(10L);
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).saveOrderItems(anyList(), eq(100L));
        verify(productRepository).decrementStock(10L, 2);
    }

    @Test
    void shouldCreateOrderForNewUser() {
        String customerEmail = "new@test.com";
        OrderCreateRequestDto request = new OrderCreateRequestDto(
                "New Customer",
                customerEmail,
                List.of(new OrderItemRequestDto(10L, 1, new BigDecimal("1000")))
        );

        User newUser = User.builder().name("New Customer").email(customerEmail).build();
        Product product = Product.builder().id(10L).name("Product").price(new BigDecimal("1000")).stock(5).build();
        Order savedOrder = Order.builder().id(100L).orderCode("ORD-2026-XYZ").userId(null).customerName("New Customer").customerEmail(customerEmail).status(OrderStatus.PENDIENTE).total(new BigDecimal("1000")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        OrderItem savedItem = OrderItem.builder().id(1L).orderId(100L).productId(10L).productName("Product").quantity(1).unitPrice(new BigDecimal("1000")).subtotal(new BigDecimal("1000")).build();

        when(userRepository.findByEmail(customerEmail)).thenReturn(Mono.empty());
        when(productRepository.findById(10L)).thenReturn(Mono.just(product));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(savedOrder));
        when(orderRepository.saveOrderItems(anyList(), eq(100L))).thenReturn(Flux.just(savedItem));
        when(productRepository.decrementStock(10L, 1)).thenReturn(Mono.just(1));
        // Mock findByOrderCode for generateUniqueCode
        when(orderRepository.findByOrderCode(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.createOrder(customerEmail, request))
                .expectNextMatches(dto -> dto.customerName().equals("New Customer"))
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionWhenItemsEmpty() {
        OrderCreateRequestDto request = new OrderCreateRequestDto("Juan", "juan@test.com", List.of());

        StepVerifier.create(orderService.createOrder("juan@test.com", request))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void voidThrowBusinessExceptionWhenNullItems() {
        OrderCreateRequestDto request = new OrderCreateRequestDto("Juan", "juan@test.com", null);

        StepVerifier.create(orderService.createOrder("juan@test.com", request))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldThrowBusinessExceptionWhenNoEmail() {
        OrderCreateRequestDto request = new OrderCreateRequestDto("Juan", null, List.of(new OrderItemRequestDto(10L, 1, new BigDecimal("1000"))));

        StepVerifier.create(orderService.createOrder(null, request))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFound() {
        OrderCreateRequestDto request = new OrderCreateRequestDto("Juan", "juan@test.com", List.of(new OrderItemRequestDto(999L, 1, new BigDecimal("1000"))));

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.just(User.builder().id(1L).email("juan@test.com").build()));
        when(productRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(orderService.createOrder("juan@test.com", request))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void shouldThrowInsufficientStockExceptionWhenStockTooLow() {
        OrderCreateRequestDto request = new OrderCreateRequestDto("Juan", "juan@test.com", List.of(new OrderItemRequestDto(10L, 10, new BigDecimal("1000"))));

        User user = User.builder().id(1L).email("juan@test.com").build();
        Product product = Product.builder().id(10L).name("Product").price(new BigDecimal("1000")).stock(5).build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.just(user));
        when(productRepository.findById(10L)).thenReturn(Mono.just(product));

        StepVerifier.create(orderService.createOrder("juan@test.com", request))
                .expectError(InsufficientStockException.class)
                .verify();
    }

    @Test
    void shouldGetOrderStatusSuccessfully() {
        String orderCode = "ORD-2026-ABC";
        Order order = Order.builder()
                .id(100L)
                .orderCode(orderCode)
                .customerName("Juan Pérez")
                .customerEmail("juan@test.com")
                .status(OrderStatus.EN_TRANSITO)
                .total(new BigDecimal("17000.00"))
                .createdAt(LocalDateTime.of(2026, 1, 15, 10, 30))
                .build();

        OrderItem item = OrderItem.builder()
                .id(1L)
                .orderId(100L)
                .productId(10L)
                .productName("Rolex Submariner")
                .quantity(2)
                .unitPrice(new BigDecimal("8500.00"))
                .subtotal(new BigDecimal("17000.00"))
                .build();

        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Mono.just(order));
        when(orderRepository.findItemsByOrderId(100L)).thenReturn(Flux.just(item));

        StepVerifier.create(orderService.getOrderStatus(orderCode))
                .expectNextMatches(dto -> {
                    assertThat(dto.orderId()).isEqualTo(orderCode);
                    assertThat(dto.status()).isEqualTo("En Tránsito");
                    assertThat(dto.items()).hasSize(1);
                    return true;
                })
                .verifyComplete();

        verify(orderRepository).findByOrderCode(orderCode);
        verify(orderRepository).findItemsByOrderId(100L);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFound() {
        when(orderRepository.findByOrderCode("ORD-2026-NOTFOUND")).thenReturn(Mono.empty());

        StepVerifier.create(orderService.getOrderStatus("ORD-2026-NOTFOUND"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void shouldThrowBusinessExceptionWhenOrderIdBlank() {
        StepVerifier.create(orderService.getOrderStatus(""))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(orderService.getOrderStatus("   "))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void shouldNormalizeOrderCodeToUpperCase() {
        Order order = Order.builder().id(100L).orderCode("ORD-2026-ABC").customerName("Test").customerEmail("test@test.com").status(OrderStatus.PENDIENTE).total(new BigDecimal("100")).createdAt(LocalDateTime.now()).build();
        OrderItem item = OrderItem.builder().id(1L).orderId(100L).productId(10L).productName("Product").quantity(1).unitPrice(new BigDecimal("100")).subtotal(new BigDecimal("100")).build();

        when(orderRepository.findByOrderCode("ORD-2026-ABC")).thenReturn(Mono.just(order));
        when(orderRepository.findItemsByOrderId(100L)).thenReturn(Flux.just(item));

        StepVerifier.create(orderService.getOrderStatus("  ord-2026-abc  "))
                .expectNextMatches(dto -> dto.orderId().equals("ORD-2026-ABC"))
                .verifyComplete();

        verify(orderRepository).findByOrderCode("ORD-2026-ABC");
    }
}