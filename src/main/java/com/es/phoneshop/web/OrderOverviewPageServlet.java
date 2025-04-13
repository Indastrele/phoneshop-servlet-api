package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.dao.ArrayListOrderDao;
import com.es.phoneshop.model.order.dao.OrderDao;
import com.es.phoneshop.model.order.service.DefaultOrderService;
import com.es.phoneshop.model.order.service.OrderService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/overviewPage.jsp";
    private static final String ORDER = "order";
    private OrderDao orderDao;

    public OrderOverviewPageServlet() {
    }

    public OrderOverviewPageServlet(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (orderDao == null) orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Order order = orderDao.getOrderBySecureId(request.getPathInfo().substring(1));
        request.setAttribute(ORDER, order);

        request.getRequestDispatcher(ORDER_OVERVIEW_JSP).forward(request, response);
    }
}
