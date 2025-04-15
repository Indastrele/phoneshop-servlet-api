package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.cart.NotEnoughStockException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

public class CartPageServlet extends HttpServlet {
    private static final String CART_PAGE_JSP = "/WEB-INF/pages/cartPage.jsp";
    private static final String CART = "cart";
    private static final String MESSAGE = "message";
    private static final String QUANTITY = "quantity";
    private static final String PRODUCT_ID = "productId";
    private static final String ERRORS = "errors";
    private CartService cartService;

    public CartPageServlet() {
    }

    public CartPageServlet(CartService cartService) {
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
        request.setAttribute(CART, cartService == null ? null : cartService.getCart(request));

        HttpSession session = request.getSession();
        String message = (String)session.getAttribute(MESSAGE);
        if (message != null) {
            request.setAttribute(MESSAGE, message);
            session.setAttribute(MESSAGE, null);
        }

        request.getRequestDispatcher(CART_PAGE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] quantityList = request.getParameterValues(QUANTITY);
        String[] productIdList = request.getParameterValues(PRODUCT_ID);
        HashMap<Long, String> errors = new HashMap<>();

            var length = quantityList.length;
            for (int i = 0; i < length; i++) {
                Long productId = Long.valueOf(productIdList[i]);

                try {
                    cartService.update(
                            cartService.getCart(request),
                            productId,
                            convertToLong(quantityList[i], request)
                    );
                }  catch (NotEnoughStockException ex) {
                    errors.put(productId, ex.getMessage());
                } catch (ParseException ex) {
                    errors.put(productId, "Not a number");
                }
            }

        if (errors.isEmpty()) {
            request.getSession().setAttribute(MESSAGE, "Successful update");
            response.sendRedirect(String.format("%s/cart", request.getContextPath()));
        } else {
            request.setAttribute(ERRORS, errors);
            doGet(request, response);
        }
    }

    private int convertToLong(String source, HttpServletRequest request) throws ParseException {
        return NumberFormat.getNumberInstance(request.getLocale())
                .parse(source)
                .intValue();
    }
}
