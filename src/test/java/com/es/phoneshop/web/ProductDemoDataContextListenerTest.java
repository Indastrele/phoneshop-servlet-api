package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemoDataContextListenerTest {
    @Mock
    private ServletContext context;
    @Mock
    private ProductDao dao;

    private ProductDemoDataContextListener demoDataContextListener;

    @Before
    public void setup() {
        dao = new ProductDao() {
            private final List<Product> products = new ArrayList<>();

            @Override
            public Product getProduct(Long id) {
                return null;
            }

            @Override
            public List<Product> findProducts(String query, SortField field, SortOrder order) {
                return products;
            }

            @Override
            public void save(Product product) {
                products.add(product);
            }

            @Override
            public void delete(Long id) {

            }
        };

        demoDataContextListener = new ProductDemoDataContextListener(dao);
    }

    @Test
    public void testContextInitialized() {
        assertTrue(dao.findProducts(null, null, null).isEmpty());
        when(context.getInitParameter("insertDemoData")).thenReturn("true");

        demoDataContextListener.contextInitialized(new ServletContextEvent(context));

        assertFalse(dao.findProducts(null, null, null).isEmpty());
    }
}
