package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService{
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static DefaultCartService instance;

    public static synchronized DefaultCartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }

        return instance;
    }

    private ProductDao dao;
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private DefaultCartService() {
        this.dao = ArrayListProductDao.getInstance();
    }

    protected DefaultCartService(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        rwLock.readLock().lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);

            if (cart == null) {
                cart = new Cart();
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }

            return cart;
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException {
        rwLock.writeLock().lock();
        try {
            int quantityInCart = cart.getCart().stream()
                    .filter(item -> productId.equals(item.getProduct().getId()))
                    .findFirst()
                    .orElse(new CartItem(null, 0))
                    .getQuantity();
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
