package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.DescriptionSearchType;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao extends ProductDao {
    private static volatile ProductDao instance;
    
    private final TreeMap<Long, Integer> relevance = new TreeMap<>();

    public static ProductDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }

        return instance;
    }

    private ArrayListProductDao() {

    }

    public ArrayListProductDao(ArrayList<Product> products) {
        this.items = products;
    }

    @Override
    public List<Product> findProducts(String query, SortField field, SortOrder order) {
        rwLock.readLock().lock();
        try {
            if ((field == null || order == null) && query == null) {
                return getProducts();
            }

            Comparator<Product> comparator;

            if (field == null || order == null) {
                relevance.clear();
                items.forEach(product -> relevance.put(product.getId(), calculateProductRelevance(product, query)));

                comparator = Comparator.comparingInt(product -> relevance.get(product.getId()));

                return getFilteredProducts(comparator, query);
            }

            comparator = Comparator.comparing(product -> {
               if (SortField.DESCRIPTION == field) {
                   return (Comparable) product.getDescription();
               } else {
                   return (Comparable) product.getPrice();
               }
            });

            if (order == SortOrder.DESC) {
                comparator = comparator.reversed();
            }

            return getFilteredProducts(comparator, query);
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    private List<Product> getProducts() {
        return items.stream()
                .filter(product -> product.getPrice() != null && product.getStock() != 0)
                .toList();
    }

    private List<Product> getFilteredProducts(Comparator<Product> comparator, String query) {
        return items.stream()
                .filter(product -> {
                    if (product.getPrice() == null || product.getStock() == 0) return false;

                    if (query == null || query.isEmpty()) return true;

                    List<String> tokenizedQuery = List.of(query.toLowerCase().split(" "));
                    String description = product.getDescription().toLowerCase();
                    return tokenizedQuery.stream().allMatch(description::contains);
                })
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private int calculateProductRelevance(Product product, String query) {
        List<String> tokenizedQuery = List.of(query.toLowerCase().split(" "));
        List<String> intersections = Stream.of(product.getDescription().toLowerCase().split(" "))
                .distinct()
                .filter(tokenizedQuery::contains)
                .toList();

        return tokenizedQuery.size() - intersections.size();
    }

    @Override
    public List<Product> findProductsByCriteria(String description, BigDecimal minPrice, BigDecimal maxPrice,
                                                DescriptionSearchType searchType) {
        rwLock.readLock().lock();
        try {
            if (searchType == null) {
                return new ArrayList<>();
            }
            if ((description == null || description.isEmpty()) && minPrice == null && maxPrice == null) {
                return getProducts();
            }

            return new ArrayList<>();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        rwLock.writeLock().lock();
        try {
            items.removeIf(product -> id.equals(product.getId()));
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }
}
