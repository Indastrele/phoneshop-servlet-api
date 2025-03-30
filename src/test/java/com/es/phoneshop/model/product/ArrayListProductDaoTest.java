package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts(null, null, null).isEmpty());
    }

    @Test
    public void testSaveProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null)));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(product::equals));

        productDao.save(product);

        assertTrue(product.getId() > 0);

        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test", result.getCode());
    }

    @Test
    public void testZeroStockProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null)));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(product::equals));

        productDao.save(product);

        assertTrue(product.getId() > 0);
        assertNotNull(productDao.getProduct(product.getId()));

        Product result = productDao.findProducts(null, null, null).stream()
                .filter(product1 -> product.getId().equals(product1.getId()))
                .findAny()
                .orElse(null);
        assertNull(result);
    }

    @Test
    public void testNullPriceProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", null, usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", null);

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(product::equals));

        productDao.save(product);

        assertTrue(product.getId() > 0);
        assertNotNull(productDao.getProduct(product.getId()));

        Product result = productDao.findProducts(null, null, null).stream()
                .filter(product1 -> product.getId().equals(product1.getId()))
                .findAny()
                .orElse(null);
        assertNull(result);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null)));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(product::equals));

        productDao.save(product);

        assertTrue(product.getId() > 0);
        assertNotNull(productDao.getProduct(product.getId()));

        productDao.delete(product.getId());

        // It will throw ProductNotFound, if product will be deleted successfully
        productDao.getProduct(product.getId());
    }

    @Test
    public void testFindProductWithOutParameters() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertEquals(products, productDao.findProducts(null, null, null));
    }

    @Test
    public void testFindProductWithQueryParameter() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(1000), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(2)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertEquals(products, productDao.findProducts("a", null, null));
        assertEquals(List.of(products.get(1)), productDao.findProducts("samsung s iii", null, null));
        assertEquals(List.of(products.get(2)), productDao.findProducts("apple", null, null));
        assertEquals(List.of(products.get(0), products.get(1)), productDao.findProducts("samsung s", null, null));
    }

    @Test
    public void testFindProductWithFieldParameter() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertEquals(products, productDao.findProducts(null, SortField.DESCRIPTION, null));
        assertEquals(products, productDao.findProducts(null, SortField.PRICE, null));
    }

    @Test
    public void testFindProductWithOrderParameter() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertEquals(products, productDao.findProducts(null, null, SortOrder.ASC));
        assertEquals(products, productDao.findProducts(null, null, SortOrder.DESC));
    }

    @Test
    public void testFindProductWithQueryAndFieldParameters() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.add(new Product("test3", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(1000), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(2)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertEquals(products, productDao.findProducts("a", SortField.DESCRIPTION, null));
        assertEquals(List.of(products.get(1)), productDao.findProducts("samsung s iii", SortField.DESCRIPTION, null));
        assertEquals(List.of(products.get(2)), productDao.findProducts("apple", SortField.DESCRIPTION, null));
        assertEquals(List.of(products.get(0), products.get(1)), productDao.findProducts("samsung s", SortField.DESCRIPTION, null));

        assertEquals(products, productDao.findProducts("a", SortField.PRICE, null));
        assertEquals(List.of(products.get(1)), productDao.findProducts("samsung s iii", SortField.PRICE, null));
        assertEquals(List.of(products.get(2)), productDao.findProducts("apple", SortField.PRICE, null));
        assertEquals(List.of(products.get(0), products.get(1)), productDao.findProducts("samsung s", SortField.PRICE, null));
    }

    @Test
    public void testFindProductWithQueryAndOrderParameters() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.add(new Product("test3", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(1000), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(2)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertEquals(products, productDao.findProducts("a", null, SortOrder.ASC));
        assertEquals(List.of(products.get(1)), productDao.findProducts("samsung s iii", null, SortOrder.ASC));
        assertEquals(List.of(products.get(2)), productDao.findProducts("apple", null, SortOrder.ASC));
        assertEquals(List.of(products.get(0), products.get(1)), productDao.findProducts("samsung s", null, SortOrder.ASC));

        assertEquals(products, productDao.findProducts("a", null, SortOrder.DESC));
        assertEquals(List.of(products.get(1)), productDao.findProducts("samsung s iii", null, SortOrder.DESC));
        assertEquals(List.of(products.get(2)), productDao.findProducts("apple", null, SortOrder.DESC));
        assertEquals(List.of(products.get(0), products.get(1)), productDao.findProducts("samsung s", null, SortOrder.DESC));
    }

    @Test
    public void testFindProductWithFieldAndOrderParameters() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(1000), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertNotEquals(products, productDao.findProducts(null, SortField.DESCRIPTION, SortOrder.ASC));
        assertEquals(products, productDao.findProducts(null, SortField.DESCRIPTION, SortOrder.DESC));

        assertEquals(products, productDao.findProducts(null, SortField.PRICE, SortOrder.ASC));
        assertNotEquals(products, productDao.findProducts(null, SortField.PRICE, SortOrder.DESC));
    }

    @Test
    public void testFindProductWithAllParameters() {
        Currency usd = Currency.getInstance("USD");

        List<Product> products = new ArrayList<>();
        products.add(new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(0)::equals));

        products.add(new Product("test2", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(1000), null))));

        assertTrue(productDao.findProducts(null, null, null).stream().noneMatch(products.get(1)::equals));

        products.forEach(product -> productDao.save(product));
        products.forEach(product -> {
            assertTrue(product.getId() > 0);
            assertNotNull(productDao.getProduct(product.getId()));
        });

        assertNotEquals(products, productDao.findProducts("a", SortField.DESCRIPTION, SortOrder.ASC));
        assertEquals(List.of(products.get(0)), productDao.findProducts("samsung", SortField.DESCRIPTION, SortOrder.ASC));
        assertEquals(List.of(products.get(1)), productDao.findProducts("apple", SortField.DESCRIPTION, SortOrder.ASC));

        assertEquals(products, productDao.findProducts("a", SortField.PRICE, SortOrder.ASC));
        assertEquals(List.of(products.get(0)), productDao.findProducts("samsung", SortField.PRICE, SortOrder.ASC));
        assertEquals(List.of(products.get(1)), productDao.findProducts("apple", SortField.PRICE, SortOrder.ASC));

        assertEquals(products, productDao.findProducts("a", SortField.DESCRIPTION, SortOrder.DESC));
        assertEquals(List.of(products.get(0)), productDao.findProducts("samsung", SortField.DESCRIPTION, SortOrder.DESC));
        assertEquals(List.of(products.get(1)), productDao.findProducts("apple", SortField.DESCRIPTION, SortOrder.DESC));

        assertNotEquals(products, productDao.findProducts("a", SortField.PRICE, SortOrder.DESC));
        assertEquals(List.of(products.get(0)), productDao.findProducts("samsung", SortField.PRICE, SortOrder.DESC));
        assertEquals(List.of(products.get(1)), productDao.findProducts("apple", SortField.PRICE, SortOrder.DESC));
    }
}
