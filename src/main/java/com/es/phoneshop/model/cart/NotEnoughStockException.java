package com.es.phoneshop.model.cart;

public class NotEnoughStockException extends RuntimeException{
    public NotEnoughStockException(Long productId, int stockAvailable, int requestedQuantity) {
        super(
                String.format(
                        "Not enough stock(id: %d;available: %d; requested: %d)",
                        productId,
                        stockAvailable,
                        requestedQuantity
                )
        );
    }
}
