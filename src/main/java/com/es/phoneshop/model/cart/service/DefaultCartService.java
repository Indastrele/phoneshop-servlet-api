package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.exceptions.cart.NotEnoughStockException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService{
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static volatile DefaultCartService instance;

    public static DefaultCartService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }

        return instance;
    }

    private final ProductDao dao;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private DefaultCartService() {
        this.dao = ArrayListProductDao.getInstance();
    }

    public DefaultCartService(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        rwLock.readLock().lock();
        try {
            HttpSession session = request.getSession();

            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);

            if (cart == null) {
                cart = new Cart();
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }

            return cart;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException {
        rwLock.writeLock().lock();
        try {
            int quantityInCart = cart.getItemList().stream()
                    .filter(item -> productId.equals(item.getProduct().getId()))
                    .findFirst()
                    .orElse(new CartItem(null, 0))
                    .getQuantity();
            int stock = dao.getProduct(productId).getStock();

            if (quantity > stock - quantityInCart) {
                throw new NotEnoughStockException(productId, stock - quantityInCart, quantity);
            }

            if (quantityInCart > 0) {
                cart.getItemList().stream()
                        .filter(item -> productId.equals(item.getProduct().getId()))
                        .findFirst()
                        .get()
                        .setQuantity(quantity + quantityInCart);
                recalculateCart(cart);

                return;
            }

            cart.getItemList().add(new CartItem(dao.getProduct(productId), quantity));
            recalculateCart(cart);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws NotEnoughStockException {
        rwLock.writeLock().lock();
        try {
            int stock = dao.getProduct(productId).getStock();

            if (quantity > stock) {
                throw new NotEnoughStockException(productId, stock, quantity);
            }


            cart.getItemList().stream()
                    .filter(item -> productId.equals(item.getProduct().getId()))
                    .findFirst()
                    .get()
                    .setQuantity(quantity);

            recalculateCart(cart);

        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        rwLock.writeLock().lock();
        try {
            cart.getItemList().removeIf(item -> productId.equals(item.getProduct().getId()));
            recalculateCart(cart);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItemList().stream()
                .map(CartItem::getQuantity)
                .mapToInt(Integer::intValue)
                .sum()
        );

        cart.setTotalPrice(cart.getItemList().stream()
                .map(cartItem -> cartItem.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

    }
}
