package com.es.phoneshop.model.product.price_history;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedProductsService implements RecentlyViewedProductsService {
    private static final String RECENTLY_VIED_ATTRIBUTE = DefaultRecentlyViewedProductsService.class
                                                                                            .getName()
                                                                                            .concat(".products");
    private static volatile DefaultRecentlyViewedProductsService instance;

    public static DefaultRecentlyViewedProductsService getInstance() {
        if (instance == null) {
            synchronized (DefaultRecentlyViewedProductsService.class) {
                if (instance == null) {
                    instance = new DefaultRecentlyViewedProductsService();
                }
            }
        }

        return instance;
    }

    private final ProductDao dao;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private DefaultRecentlyViewedProductsService(){
        this.dao = ArrayListProductDao.getInstance();
    }

    protected DefaultRecentlyViewedProductsService(ProductDao dao) {
        this.dao = dao;
    }

    public RecentlyViewedProducts getProducts(HttpServletRequest request) {
        rwLock.readLock().lock();
        try {
            HttpSession session = request.getSession();
            var products = (RecentlyViewedProducts) session.getAttribute(RECENTLY_VIED_ATTRIBUTE);

            if (products == null) {
                products = new RecentlyViewedProducts();
                session.setAttribute(RECENTLY_VIED_ATTRIBUTE, products);
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
