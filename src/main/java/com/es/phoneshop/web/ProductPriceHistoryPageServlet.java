package com.es.phoneshop.web;

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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private static final String PRICE_HISTORY_PAGE_JSP = "/WEB-INF/pages/productPriceHistoryPage.jsp";
    private static final String PRODUCT = "product";
    private static final String RECENTLY_VIEWED = "recentlyViewed";
    private static final String PRICE_HISTORY_PAGE_JSP = "/WEB-INF/pages/productPriceHistoryPage.jsp";
    private ProductDao dao;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    public ProductPriceHistoryPageServlet() {
    }

    public ProductPriceHistoryPageServlet(ProductDao dao, RecentlyViewedProductsService recentlyViewedProductsService) {
        this.dao = dao;
        this.recentlyViewedProductsService = recentlyViewedProductsService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) {
            dao = ArrayListProductDao.getInstance();
        }

        if (recentlyViewedProductsService == null) {
            recentlyViewedProductsService = DefaultRecentlyViewedProductsService.getInstance();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = dao.getProduct(getIdFromPath(request));

        request.setAttribute(PRODUCT, product);
        request.setAttribute(
                RECENTLY_VIEWED,
                recentlyViewedProductsService == null ? null : recentlyViewedProductsService.getProducts(request));
        request.getRequestDispatcher(PRICE_HISTORY_PAGE_JSP).forward(request, response);
    }

    private Long getIdFromPath(HttpServletRequest request) {
        String idString = request.getPathInfo();
        return Long.valueOf(idString.substring(1));
    }
}
