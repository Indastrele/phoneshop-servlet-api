package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.cart.NotEnoughStockException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.price_history.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.price_history.RecentlyViewedProductsService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_PAGE_JSP = "/WEB-INF/pages/productPage.jsp";
    private static final String PRODUCT = "product";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String QUANTITY = "quantity";
    private static final String CART = "cart";
    private static final String RECENTLY_VIEWED = "recentlyViewed";
    public static final String PRODUCT_PAGE_JSP = "/WEB-INF/pages/productPage.jsp";
    private ProductDao dao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    public ProductDetailsPageServlet() {
    }

    public ProductDetailsPageServlet(
            ProductDao dao,
            CartService cartService,
            RecentlyViewedProductsService recentlyViewedProductsService) {
        this.dao = dao;
        this.cartService = cartService;
        this.recentlyViewedProductsService = recentlyViewedProductsService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) {
            dao = ArrayListProductDao.getInstance();
        }

        if (cartService == null) {
            cartService = DefaultCartService.getInstance();
        }

        if (recentlyViewedProductsService == null) {
            recentlyViewedProductsService = DefaultRecentlyViewedProductsService.getInstance();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = dao.getProduct(getIdFromPath(request));
        recentlyViewedProductsService.add(recentlyViewedProductsService.getProducts(request), product.getId());

        HttpSession session = request.getSession();
        String message = (String) session.getAttribute(MESSAGE);
        if (message != null) {
            request.setAttribute(MESSAGE, message);
            session.setAttribute(MESSAGE, null);
        }

        request.setAttribute(PRODUCT, product);
        request.setAttribute(CART, cartService == null ? null : cartService.getCart(request));
        request.setAttribute(
                RECENTLY_VIEWED,
                recentlyViewedProductsService == null ? null : recentlyViewedProductsService.getProducts(request));
        request.getRequestDispatcher(PRODUCT_PAGE_JSP).forward(request, response);
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
            request.setAttribute(ERROR, ex.getMessage());

            doGet(request, response);
            return;
        }

        request.getSession().setAttribute(MESSAGE, "Successfully added to cart");
        response.sendRedirect(String.format("%s/products%s", request.getContextPath(), request.getPathInfo()));
    }

    private Long getIdFromPath(HttpServletRequest request) {
        String idString = request.getPathInfo();
        return Long.valueOf(idString.substring(1));
    }
}
