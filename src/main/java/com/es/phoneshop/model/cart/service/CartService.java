package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.exceptions.cart.NotEnoughStockException;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException;
    void update(Cart cart, Long productId, int quantity) throws NotEnoughStockException;
    void delete(Cart cart, Long productId);
}
