package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService{
    private static DefaultCartService instance;

    public static synchronized DefaultCartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }

        return instance;
    }

    private Cart cart  = new Cart();
    private ProductDao dao;
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private DefaultCartService() {
    }

    protected DefaultCartService(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public Cart getCart() {
        rwLock.readLock().lock();
        try {
            return this.cart;
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void add(Long productId, int quantity) throws NotEnoughStockException {
        rwLock.writeLock().lock();
        try {
            int quantityInCart = cart.getCart().stream()
                    .filter(item -> productId.equals(item.getProduct().getId()))
                    .map(CartItem::getQuantity)
                    .mapToInt(Integer::intValue)
                    .sum();
            int stock = dao.getProduct(productId).getStock();

            if (quantity > stock - quantityInCart) {
                throw new NotEnoughStockException(productId, stock - quantityInCart, quantity);
            }

            if (quantityInCart > 0) {
                cart.getCart().stream()
                        .filter(item -> productId.equals(item.getProduct().getId()))
                        .findFirst()
                        .get()
                        .setQuantity(quantity + quantityInCart);

                return;
            }

            cart.getCart().add(new CartItem(dao.getProduct(productId), quantity));
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }
}
