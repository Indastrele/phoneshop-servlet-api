package com.es.phoneshop.model.order.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.order.dao.ArrayListOrderDao;
import com.es.phoneshop.model.order.dao.OrderDao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private static volatile DefaultOrderService instance;

    public static DefaultOrderService getInstance() {
        if (instance == null) {
            synchronized (DefaultOrderService.class) {
                if (instance == null) {
                    instance = new DefaultOrderService();
                }
            }
        }

        return instance;
    }

    private final OrderDao orderDao;
    private DefaultOrderService() {
        this.orderDao = ArrayListOrderDao.getInstance();
    }

    protected DefaultOrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItemList(cart.getItemList().stream().map(item -> {
            try {
                return item.clone();
            } catch (AssertionError e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalPrice());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));

        return order;
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal("5.0");
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        orderDao.save(order);
    }
}
