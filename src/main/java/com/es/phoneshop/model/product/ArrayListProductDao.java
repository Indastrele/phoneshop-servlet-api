package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }

        return instance;
    }

    private ArrayList<Product> products = new ArrayList<>();
    private long identity = 1;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private ArrayListProductDao() {

    }

    @Override
    public Product getProduct(Long id) {
        readWriteLock.readLock().lock();
        try {
            return  products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findFirst()
                    .orElse(null);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField field, SortOrder order) {
        readWriteLock.readLock().lock();
        try {
            Comparator<Product> comparator;

            if (field == null && order == null) {
                comparator = Comparator.comparingInt(product -> {
                    if (query == null) {
                        return 0;
                    }

                    List<String> tokenizedQuery = List.of(query.toLowerCase().split(" "));
                    List<String> description = List.of(product.getDescription().toLowerCase().split(" "));
                    Set<String> intersections = description.stream()
                            .distinct()
                            .filter(tokenizedQuery::contains)
                            .collect(Collectors.toSet());

                    return tokenizedQuery.size() - intersections.size();
                });

                return getFilteredProducts(comparator, query);
            }

            comparator = Comparator.comparing(product -> {
               if (SortField.description == field) {
                   return (Comparable) product.getDescription();
               } else {
                   return (Comparable) product.getPrice();
               }
            });

            if (order == SortOrder.desc) {
                comparator = comparator.reversed();
            }

            return getFilteredProducts(comparator, query);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    private List<Product> getFilteredProducts(Comparator<Product> comparator, String query) {
        return products.stream()
                .filter(product -> {
                    if (product.getPrice() == null || product.getStock() == 0) {
                        return false;
                    }

                    if (query == null || query.isEmpty()) return true;

                    List<String> tokenizedQuery = List.of(query.toLowerCase().split(" "));
                    String description = product.getDescription().toLowerCase();
                    return tokenizedQuery.stream().allMatch(description::contains);
                })
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        readWriteLock.writeLock().lock();
        try {
            var id = product.getId();
            if (id == null) {
                product.setId(identity++);
                products.add(product);
                return;
            }

            var oldProduct = getProduct(id);
            if (oldProduct == null) {
                throw new ProductNotFoundException(id);
            }

            var index = products.indexOf(oldProduct);
            product.setId(oldProduct.getId());
            products.set(index, product);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        readWriteLock.writeLock().lock();
        try {
            products.removeIf(product -> id.equals(product.getId()));
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
