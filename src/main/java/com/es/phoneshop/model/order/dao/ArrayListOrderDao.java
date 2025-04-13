package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.exceptions.order.OrderNotFoundException;
import com.es.phoneshop.model.exceptions.product.ProductNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends OrderDao {
    private static volatile OrderDao instance;

    public static OrderDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListOrderDao.class) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }

        return instance;
    }

    private ArrayListOrderDao() {

    }

    public ArrayListOrderDao(ArrayList<Order> orders) {
        this.items = orders;
    }
}
