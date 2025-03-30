package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT = "product";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String QUANTITY = "quantity";
    private static final String CART = "cart";
    private ProductDao dao;
    private CartService cartService;

    public ProductDetailsPageServlet() {
    }

    public ProductDetailsPageServlet(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) dao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = dao.getProduct(getIdFromPath(request));

        request.setAttribute(PRODUCT, product);
        request.setAttribute(CART, cartService == null ? null : cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/productPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quantity = request.getParameter(QUANTITY);
        Locale locale = request.getLocale();
        try {
            cartService.add(
                    cartService.getCart(request),
                    getIdFromPath(request),
                    NumberFormat.getInstance(locale).parse(quantity).intValue()
            );
        } catch (ParseException ex) {
            request.setAttribute(ERROR, "Not a number");

            doGet(request, response);
            return;
        } catch (NotEnoughStockException ex) {
            request.setAttribute(
                    ERROR,
                    "Not enough stock(available: "
                            + ex.getStockAvailable()
                            + "; requested: "
                            + ex.getRequestedQuantity()
                            + ")"
            );

            doGet(request, response);
            return;
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/products"
                        + request.getPathInfo()
                        + "?"
                        + MESSAGE
                        + "=Successfully added to cart"
        );
    }

    private Long getIdFromPath(HttpServletRequest request) {
        String idString = request.getPathInfo();
        return Long.valueOf(idString.substring(1));
    }
}
