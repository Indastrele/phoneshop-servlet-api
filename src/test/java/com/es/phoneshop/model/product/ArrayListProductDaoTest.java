package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product);

        assertTrue(product.getId() > 0);

        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test", result.getCode());
    }

    @Test
    public void testZeroStockProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product);

        Product result = productDao.findProducts().stream()
                .filter(product1 -> product.getId().equals(product1.getId()))
                .findAny()
                .orElse(null);
        assertNull(result);
    }

    @Test
    public void testNullPriceProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product);

        Product result = productDao.findProducts().stream()
                .filter(product1 -> product.getId().equals(product1.getId()))
                .findAny()
                .orElse(null);
        assertNull(result);
    }

    @Test
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product);

        productDao.delete(product.getId());

        Product result = productDao.getProduct(product.getId());
        assertNull(result);
    }


}
