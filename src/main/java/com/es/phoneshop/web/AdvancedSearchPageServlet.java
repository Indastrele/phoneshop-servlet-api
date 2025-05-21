package com.es.phoneshop.web;

import com.es.phoneshop.model.product.DescriptionSearchType;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {

    private static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private static final String PRODUCTS = "products";
    private static final String DESCRIPTION_SEARCH_TYPE = "descriptionSearchType";
    private static final String ERROR_PRICE_MESSAGE = "Not a number";
    private static final String MIN_PRICE = "minPrice";
    private static final String MAX_PRICE = "maxPrice";
    private static final String DESCRIPTION = "description";
    private static final String ERRORS = "errors";

    private final ProductDao productDao = ArrayListProductDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, String> errors = new HashMap<>();
        List<Product> products = productDao.findProductsByCriteria(request.getParameter(DESCRIPTION),
                getPrice(request, MIN_PRICE, errors),
                getPrice(request, MAX_PRICE, errors),
                Optional.ofNullable(request.getParameter(DESCRIPTION_SEARCH_TYPE)).map(DescriptionSearchType::getType)
                        .orElse(null));

        if (errors.isEmpty()) {
            request.setAttribute(PRODUCTS, products);
        } else {
            request.setAttribute(PRODUCTS, new ArrayList<>());
            request.setAttribute(ERRORS, errors);
        }

        request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
    }

    private BigDecimal getPrice(HttpServletRequest request, String parameterName, HashMap<String, String> errors) {
        try {
            String param = request.getParameter(parameterName);
            if (param == null || param.isEmpty()) {
                return null;
            }

            return new BigDecimal(param);
        } catch (NumberFormatException ex) {
            errors.put(parameterName, ERROR_PRICE_MESSAGE);
            return null;
        }
    }
}
