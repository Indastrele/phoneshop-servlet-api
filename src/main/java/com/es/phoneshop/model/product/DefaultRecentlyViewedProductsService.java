package com.es.phoneshop.model.product;

import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedProductsService implements RecentlyViewedProductsService {
    private static final String RECENTLY_VIED_ATTRIBUTE = DefaultRecentlyViewedProductsService.class.getName() + ".cart";
    private static DefaultRecentlyViewedProductsService instance;

    public static synchronized DefaultRecentlyViewedProductsService getInstance() {
        if (instance == null) {
            instance = new DefaultRecentlyViewedProductsService();
        }

        return instance;
    }

    private ProductDao dao = ArrayListProductDao.getInstance();

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private DefaultRecentlyViewedProductsService(){
    }

    public RecentlyViewedProducts getProducts(HttpServletRequest request) {
        rwLock.readLock().lock();
        try {
            var products = (RecentlyViewedProducts) request.getSession().getAttribute(RECENTLY_VIED_ATTRIBUTE);

            if (products == null) {
                products = new RecentlyViewedProducts();
                request.getSession().setAttribute(RECENTLY_VIED_ATTRIBUTE, products);
            }

            return products;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void add(RecentlyViewedProducts products, Long id) {
        rwLock.writeLock().lock();
        try {
            var product = dao.getProduct(id);

            if (products.getProducts().stream().anyMatch(product::equals))
            {
                products.getProducts().remove(product);
            }

            if (products.getProducts().size() > 2) {
                products.getProducts().removeLast();
            }

            products.getProducts().addFirst(product);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
