package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.RecentlyViewedProductsService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class AddItemsProductListServlet extends HttpServlet {
    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String QUANTITY = "quantity";
    private ProductDao dao;
    private CartService cartService;

    public AddItemsProductListServlet() {
    }

    public AddItemsProductListServlet(
            ProductDao dao,
            CartService cartService) {
        this.dao = dao;
        this.cartService = cartService;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) {
            dao = ArrayListProductDao.getInstance();
        }
        if (cartService == null) {
            cartService = DefaultCartService.getInstance();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quantityString = request.getParameter(QUANTITY);

        Long productId = getIdFromPath(request);

        try {
            cartService.add(
                    cartService.getCart(request),
                    productId,
                    convertToLong(quantityString, request)
            );
        } catch (NotEnoughStockException ex) {
            request.getSession().setAttribute(ERROR_MESSAGE, ex.getMessage());
            response.sendRedirect(String.format("%s/products", request.getContextPath()));
            return;
        } catch (ParseException ex) {
            request.getSession().setAttribute(ERROR_MESSAGE, String.format("%s is not a number", quantityString));
            response.sendRedirect(String.format("%s/products", request.getContextPath()));
            return;
        }

        request.getSession().setAttribute(MESSAGE, "Successful update");
        response.sendRedirect(String.format("%s/products", request.getContextPath()));
    }

    private Long getIdFromPath(HttpServletRequest request) {
        String idString = request.getPathInfo();
        return Long.valueOf(idString.substring(1));
    }

    private int convertToLong(String source, HttpServletRequest request) throws ParseException {
        return NumberFormat.getNumberInstance(request.getLocale())
                .parse(source)
                .intValue();
    }
}
