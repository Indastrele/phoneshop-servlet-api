package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {

    public static final String MESSAGE = "message";
    private CartService cartService;

    public DeleteCartItemServlet() {
    }

    public DeleteCartItemServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (cartService == null) cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = getIdFromPath(request);

        cartService.delete(
                cartService.getCart(request),
                id
        );

        response.sendRedirect(String.format(
                "%s/cart?%s=Item with id %d was deleted",
                request.getContextPath(),
                MESSAGE,
                id
        ));
    }

    private Long getIdFromPath(HttpServletRequest request) {
        String idString = request.getPathInfo();
        return Long.valueOf(idString.substring(1));
    }
}
