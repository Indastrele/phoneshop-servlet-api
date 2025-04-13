package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.order.service.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.service.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private static final String CHECKOUT_JSP = "/WEB-INF/pages/checkoutPage.jsp";
    private static final String MESSAGE = "message";
    private static final String ORDER = "order";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String ERRORS = "errors";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String DELIVERY_DATE = "deliveryDate";
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String PAYMENT_METHODS = "paymentMethods";
    public static final String DATE = "date";
    private CartService cartService;
    private OrderService orderService;

    public CheckoutPageServlet() {
    }

    public CheckoutPageServlet(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (cartService == null) cartService = DefaultCartService.getInstance();
        if (orderService == null) orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = getCart(request);
        request.setAttribute(ORDER, getOrder(cart));
        request.setAttribute(PAYMENT_METHODS, getPaymentMethods());

        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HashMap<String, String> errors = new HashMap<>();
        Order order = getOrder(getCart(request));

        setParameter(request, FIRST_NAME, errors, order::setFirstName);
        setParameter(request, LAST_NAME, errors, order::setLastName);
        setParameter(request, PHONE, errors, order::setPhone);
        setParameter(request, DELIVERY_ADDRESS, errors, order::setDeliveryAddress);
        setLocalDateParameter(request, errors, order::setDeliveryDate);
        setPaymentMethodParameter(request, errors, order::setPaymentMethod);


        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            response.sendRedirect(
                    request.getContextPath()
                    .concat( "/overview/")
                    .concat(order.getId().toString())
            );
        } else {
            request.setAttribute(ERRORS, errors);
            request.setAttribute(DATE, order.getDeliveryDate() == null ? null : order.getDeliveryDate().toString());

            doGet(request, response);
        }
    }

    private int convertToLong(String source, HttpServletRequest request) throws ParseException {
        return NumberFormat.getNumberInstance(request.getLocale())
                .parse(source)
                .intValue();
    }

    private Cart getCart(HttpServletRequest request) {
        return cartService == null ? null : cartService.getCart(request);
    }

    private Order getOrder(Cart cart) {
        if (orderService == null) {
            throw new RuntimeException("Cannot reach Order Service");
        }

        return orderService.getOrder(cart);
    }

    private List<PaymentMethod> getPaymentMethods() {
        if (orderService == null) {
            throw new RuntimeException("Cannot reach Order Service");
        }

        return orderService.getPaymentMethods();
    }

    private void setParameter(
            HttpServletRequest request,
            String name,
            HashMap<String, String> errors,
            Consumer<String> consumer
    ) {
        String value = request.getParameter(name);

        if (value == null || value.length() == 0) {
            errors.put(name, "Value is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setLocalDateParameter(
            HttpServletRequest request,
            HashMap<String, String> errors,
            Consumer<LocalDate> consumer
    ) {
        String value = request.getParameter(DELIVERY_DATE);
        //Locale locale = request.getLocale();

        if (value == null || value.length() == 0) {
            errors.put(DELIVERY_DATE, "Value is required");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")/*.withLocale(locale)*/;
            LocalDate date = LocalDate.parse(value, formatter);

            consumer.accept(date);
        }
    }

    private void setPaymentMethodParameter(
            HttpServletRequest request,
            HashMap<String, String> errors,
            Consumer<PaymentMethod> consumer
    ) {
        String value = request.getParameter(PAYMENT_METHOD);

        if (value == null || value.length() == 0) {
            errors.put(PAYMENT_METHOD, "Value is required");
            return;
        }

        PaymentMethod method = PaymentMethod.getMethodOf(value);
        if (method == null) {
            errors.put(PAYMENT_METHOD, "Value is incorrect");
        } else {
            consumer.accept(method);
        }
    }
}
