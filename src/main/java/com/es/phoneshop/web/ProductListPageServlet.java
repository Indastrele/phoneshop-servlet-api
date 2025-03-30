package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private static final String QUERY = "query";
    private static final String ORDER = "order";
    private static final String SORT = "sort";
    private ProductDao dao;

    public ProductListPageServlet() {
    }

    public ProductListPageServlet(ProductDao dao) {
        this.dao = dao;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (dao == null) dao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Todo: !! ПЕРЕПИСАТЬ ВСЕ ТЕСТЫ ПОД ЧИСТУЮ
        request.setAttribute(
                "products",
                dao.findProducts(
                        request.getParameter(QUERY),
                        Optional.ofNullable(request.getParameter(SORT)).map(SortField::getField).orElse(null),
                        Optional.ofNullable(request.getParameter(ORDER)).map(SortOrder::getOrder).orElse(null)
                )
        );
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
