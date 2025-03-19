package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemoDataContextListenerTest {
    @Mock
    private ServletContext context;
    @Mock
    private ProductDao dao;

    private ProductDemoDataContextListener demoDataContextListener = new ProductDemoDataContextListener();

    @Before
    public void setup() {
        dao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testContextInitialized() {
        assertTrue(dao.findProducts(null, null, null).isEmpty());
        when(context.getInitParameter("insertDemoData")).thenReturn("true");

        demoDataContextListener.contextInitialized(new ServletContextEvent(context));

        assertFalse(dao.findProducts(null, null, null).isEmpty());
    }
}
