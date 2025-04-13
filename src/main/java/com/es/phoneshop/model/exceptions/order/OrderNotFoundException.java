package com.es.phoneshop.model.exceptions.order;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message) {
        super(message);
    }
}
