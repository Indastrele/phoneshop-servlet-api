package com.es.phoneshop.model.exceptions.cart;

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
