package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private static final String MINICART_JSP = "/WEB-INF/pages/minicart.jsp";
    private static final String CART = "cart";
    private CartService cartService;

    public MiniCartServlet() {
    }

    public MiniCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (cartService == null) {
            cartService = DefaultCartService.getInstance();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(CART, cartService.getCart(request));
        request.getRequestDispatcher(MINICART_JSP).include(request, response);
    }
}
