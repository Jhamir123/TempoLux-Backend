package com.TempoLux.crosscutting.exception;

/**
 * Stock insuficiente para confirmar un pedido.
 */
public class InsufficientStockException extends RuntimeException {

    private final Long productId;

    public InsufficientStockException(Long productId) {
        super("Stock insuficiente para el producto con id: " + productId);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
