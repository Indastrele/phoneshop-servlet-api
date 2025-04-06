package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class RecentlyViewedProducts implements Serializable {
    private final Deque<Product> products;

    public RecentlyViewedProducts() {
        products = new ArrayDeque<>();
    }

    public Deque<Product> getProducts() {
        return products;
    }
}
