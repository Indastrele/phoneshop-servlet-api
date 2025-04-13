package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.exceptions.cart.NotEnoughStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.price_history.ProductPriceChangeDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    @Mock
    private ProductDao dao;

    private final Cart cart = new Cart();

    private CartService cartService;

    @Before
    public void setup() {
        when(dao.getProduct(1L)).thenReturn(new Product(1L, "test", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 2, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        cartService = new DefaultCartService(dao);
    }

    @Test
    public void testAdd() throws NotEnoughStockException {
        assertTrue(cart.getItemList().stream().noneMatch(item -> item.getProduct().getId() == 1L));

        cartService.add(cart, 1L, 1);

        assertTrue(cart.getItemList().stream().anyMatch(item -> item.getProduct().getId() == 1L));
    }

    @Test(expected = NotEnoughStockException.class)
    public void testAddWithOutEnoughStock() throws NotEnoughStockException {
        assertTrue(cart.getItemList().stream().noneMatch(item -> item.getProduct().getId() == 1L));

        cartService.add(cart, 1L, 3);
    }
}
