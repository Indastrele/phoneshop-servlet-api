package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.dao.GenericDao;
import com.es.phoneshop.model.exceptions.product.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.util.List;

public abstract class ProductDao extends GenericDao<Product, ProductNotFoundException> {
    public Product getProduct(Long id) throws ProductNotFoundException {
        return getItem(id, new ProductNotFoundException("No such product: id ".concat(id.toString())));
    }
    public abstract List<Product> findProducts(String query, SortField field, SortOrder order);
    public void save(Product product) {
        save(product, new ProductNotFoundException("No such product"));
    };
    public abstract void delete(Long id);
}
