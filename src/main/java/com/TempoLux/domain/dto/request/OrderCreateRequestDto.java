package com.TempoLux.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Cuerpo del endpoint POST /api/v1/orders.
 * El cliente queda asociado al usuario autenticado (token JWT); los datos
 * de contacto solo se usan como respaldo para compras de invitados.
 */
public record OrderCreateRequestDto(
        String customerName,

        @Email(message = "El correo de contacto no tiene un formato válido")
        String customerEmail,

        @Valid
        @NotEmpty(message = "El pedido debe contener al menos un ítem")
        List<OrderItemRequestDto> items) {
}
