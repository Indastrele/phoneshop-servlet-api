package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.RecentlyViewedProductsService;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private static final String QUERY = "query";
    private static final String ORDER = "order";
    private static final String SORT = "sort";
    private static final String PRODUCTS = "products";
    private static final String CART = "cart";
    private static final String RECENTLY_VIEWED = "recentlyViewed";
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

        if (dao == null) dao = ArrayListProductDao.getInstance();
        if (cartService == null) cartService = DefaultCartService.getInstance();

        if (recentlyViewedProductsService == null) {
            recentlyViewedProductsService = DefaultRecentlyViewedProductsService.getInstance();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
