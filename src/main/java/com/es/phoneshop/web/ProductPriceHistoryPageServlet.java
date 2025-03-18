package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ProductDao dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getPathInfo();
        Product product = dao.getProduct(Long.valueOf(id.substring(1)));
        if (product == null) {
            throw new ProductNotFoundException(Long.valueOf(id.substring(1)));
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistoryPage.jsp").forward(request, response);
    }
}
