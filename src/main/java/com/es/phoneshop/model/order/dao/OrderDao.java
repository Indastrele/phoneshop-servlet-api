package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.dao.GenericDao;
import com.es.phoneshop.model.exceptions.order.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public abstract class OrderDao extends GenericDao<Order, OrderNotFoundException> {
    public Order getOrder(Long id) throws OrderNotFoundException {
        return getItem(id, new OrderNotFoundException("No such order"));
    }
    public void save(Order order){
        save(order, new OrderNotFoundException("No such order"));
    }
}
