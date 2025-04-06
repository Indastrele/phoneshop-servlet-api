package com.es.phoneshop.model.product;

import java.util.ArrayDeque;
import java.util.Deque;

public class RecentlyViewedProducts {
    private final Deque<Product> products;

    public RecentlyViewedProducts() {
        products = new ArrayDeque<>();
    }

    public Deque<Product> getProducts() {
        return products;
    }
}
