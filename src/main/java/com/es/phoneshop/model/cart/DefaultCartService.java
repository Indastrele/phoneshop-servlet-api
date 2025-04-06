package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DefaultCartService implements CartService{
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static volatile DefaultCartService instance;

    public static synchronized DefaultCartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }

        return instance;
    }

    private final ProductDao dao;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

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
                recalculateCart(cart);

                return;
            }

            cart.getCart().add(new CartItem(dao.getProduct(productId), quantity));
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


            cart.getCart().stream()
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
            cart.getCart().removeIf(item -> productId.equals(item.getProduct().getId()));
            recalculateCart(cart);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getCart().stream()
                .map(CartItem::getQuantity)
                .mapToInt(Integer::intValue)
                .sum()
        );

        cart.setTotalPrice(cart.getCart().stream()
                .map(cartItem -> cartItem.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

    }
}
