package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private CartService cartService;

    public MiniCartServlet() {
    }

    public MiniCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (cartService == null) cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/minicart.jsp").include(request, response);
    }
}
