package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;
    
    private TreeMap<Long, Integer> relevance = new TreeMap<>();

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
                    .orElseThrow(() -> new ProductNotFoundException(id));
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField field, SortOrder order) {
        readWriteLock.readLock().lock();
        try {
            if ((field == null || order == null) && query == null) {
                return getProducts();
            }

            Comparator<Product> comparator;

            if (field == null || order == null) {
                relevance.clear();
                products.stream()
                        .forEach(product -> relevance.put(product.getId(), calculateProductRelevance(product, query)));

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
            readWriteLock.readLock().unlock();
        }
    }

    private List<Product> getProducts() {
        return products.stream()
                .filter(product -> product.getPrice() != null && product.getStock() != 0)
                .toList();
    }

    private List<Product> getFilteredProducts(Comparator<Product> comparator, String query) {
        return products.stream()
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
