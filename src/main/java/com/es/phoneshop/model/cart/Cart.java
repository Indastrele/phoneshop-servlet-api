package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private final List<CartItem> cart;

    public Cart() {
        this.cart = new ArrayList<>();
    }

    public List<CartItem> getCart() {
        return cart;
    }
}
