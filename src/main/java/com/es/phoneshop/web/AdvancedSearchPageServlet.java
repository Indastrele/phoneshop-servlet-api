package com.es.phoneshop.web;

import com.es.phoneshop.model.product.DescriptionSearchType;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {

    private static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private static final String PRODUCTS = "products";
    private static final String DESCRIPTION_SEARCH_TYPE = "descriptionSearchType";

    private final ProductDao productDao = ArrayListProductDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productDao.findProductsByCriteria("", null, null,
                Optional.ofNullable(request.getParameter(DESCRIPTION_SEARCH_TYPE)).map(DescriptionSearchType::getType)
                        .orElse(null));
        request.setAttribute(PRODUCTS, products);

        request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
    }
}
