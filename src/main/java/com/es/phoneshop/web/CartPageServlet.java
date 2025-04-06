package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.TreeMap;

public class CartPageServlet extends HttpServlet {
    private static final String CART = "cart";
    private static final String MESSAGE = "message";
    private static final String QUANTITY = "quantity";
    private static final String PRODUCT_ID = "productId";
    private static final String ERROR = "error";
    private CartService cartService;

    public CartPageServlet() {
    }

    public CartPageServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (cartService == null) cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(CART, cartService == null ? null : cartService.getCart(request));

        request.getRequestDispatcher("/WEB-INF/pages/cartPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] quantityList = request.getParameterValues(QUANTITY);
        String[] productIdList = request.getParameterValues(PRODUCT_ID);
        TreeMap<Long, String> errors = new TreeMap<>();

            var length = quantityList.length;
            for (int i = 0; i < length; i++) {
                Long productId = Long.valueOf(productIdList[i]);

                int quantity = 0;

                try {
                    quantity = convertToLong(quantityList[i], request);
                } catch (ParseException ex) {
                    errors.put(productId, "Not a number");
                }

                try {
                    cartService.update(
                            cartService.getCart(request),
                            productId,
                            quantity
                    );
                }  catch (NotEnoughStockException ex) {
                    errors.put(productId, ex.getMessage());
                }
            }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?" + MESSAGE + "=Successful update");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private int convertToLong(String source, HttpServletRequest request) throws ParseException {
        return NumberFormat.getNumberInstance(request.getLocale())
                .parse(source)
                .intValue();
    }
}
