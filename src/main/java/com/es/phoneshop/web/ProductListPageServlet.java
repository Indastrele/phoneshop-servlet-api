package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.price_history.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.price_history.RecentlyViewedProductsService;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCT_LIST_JSP = "/WEB-INF/pages/productList.jsp";
    private static final String QUERY = "query";
    private static final String ORDER = "order";
    private static final String SORT = "sort";
    private static final String PRODUCTS = "products";
    private static final String CART = "cart";
    private static final String RECENTLY_VIEWED = "recentlyViewed";
    private static final String MESSAGE = "message";
    private static final String ERROR_MESSAGE = "errorMessage";
    private ProductDao dao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    public ProductListPageServlet() {
    }

    public ProductListPageServlet(
            ProductDao dao,
            CartService cartService,
            RecentlyViewedProductsService recentlyViewedProductsService) {
        this.dao = dao;
        this.cartService = cartService;
        this.recentlyViewedProductsService = recentlyViewedProductsService;
    }

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String message = (String) session.getAttribute(MESSAGE);
        if (message != null) {
            request.setAttribute(MESSAGE, message);
            session.setAttribute(MESSAGE, null);
        }

        String errorMessage = (String) session.getAttribute(ERROR_MESSAGE);
        if (errorMessage != null) {
            request.setAttribute(ERROR_MESSAGE, errorMessage);
            session.setAttribute(ERROR_MESSAGE, null);
        }

        request.setAttribute(
                PRODUCTS,
                dao.findProducts(
                        request.getParameter(QUERY),
                        Optional.ofNullable(request.getParameter(SORT)).map(SortField::getField).orElse(null),
                        Optional.ofNullable(request.getParameter(ORDER)).map(SortOrder::getOrder).orElse(null)
                )
        );
        request.setAttribute(CART, cartService == null ? null : cartService.getCart(request));
        request.setAttribute(
                RECENTLY_VIEWED,
                recentlyViewedProductsService == null ? null : recentlyViewedProductsService.getProducts(request));
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }
}
