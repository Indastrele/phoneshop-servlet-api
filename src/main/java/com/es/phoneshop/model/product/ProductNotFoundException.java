package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("id ".concat(id.toString()));
    }
}
