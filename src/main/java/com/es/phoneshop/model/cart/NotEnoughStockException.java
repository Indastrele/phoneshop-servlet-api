package com.es.phoneshop.model.cart;

public class NotEnoughStockException extends Exception{
    private Long productId;
    private int stockAvailable;
    private int requestedQuantity;

    public NotEnoughStockException(Long productId, int stockAvailable, int requestedQuantity) {
        this.productId = productId;
        this.stockAvailable = stockAvailable;
        this.requestedQuantity = requestedQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
