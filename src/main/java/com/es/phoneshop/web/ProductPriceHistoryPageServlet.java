package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.RecentlyViewedProductsService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private static final String PRODUCT = "product";
    private static final String RECENTLY_VIEWED = "recentlyViewed";
    private ProductDao dao;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    public ProductPriceHistoryPageServlet() {
    }

    public ProductPriceHistoryPageServlet(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) dao = ArrayListProductDao.getInstance();

        if (recentlyViewedProductsService == null) {
            recentlyViewedProductsService = DefaultRecentlyViewedProductsService.getInstance();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getPathInfo();
        Product product = dao.getProduct(Long.valueOf(id.substring(1)));

        request.setAttribute(PRODUCT, product);
        request.setAttribute(
                RECENTLY_VIEWED,
                recentlyViewedProductsService == null ? null : recentlyViewedProductsService.getProducts(request));
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistoryPage.jsp").forward(request, response);
    }
}
