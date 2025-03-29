package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT = "product";
    private ProductDao dao;

    public ProductDetailsPageServlet() {
    }

    public ProductDetailsPageServlet(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) dao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getPathInfo();
        Product product = dao.getProduct(Long.valueOf(id.substring(1)));

        request.setAttribute(PRODUCT, product);
        request.getRequestDispatcher("/WEB-INF/pages/productPage.jsp").forward(request, response);
    }
}
