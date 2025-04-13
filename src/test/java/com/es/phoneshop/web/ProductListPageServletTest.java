package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.price_history.RecentlyViewedProducts;
import com.es.phoneshop.model.product.price_history.RecentlyViewedProductsService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ArrayListProductDao productDao;
    @Mock
    private DefaultCartService cartService;
    @Mock
    private RecentlyViewedProductsService recentlyViewedProductsService;
    @Mock
    private Cart cart;
    @Mock
    private RecentlyViewedProducts recentlyViewedProducts;

    private ProductListPageServlet servlet;

    @Before
    public void setup() throws ServletException {
        when(productDao.findProducts(any(), any(), any())).thenReturn(new ArrayList<>());
        when(recentlyViewedProductsService.getProducts(request)).thenReturn(recentlyViewedProducts);
        when(cartService.getCart(request)).thenReturn(cart);

        servlet = new ProductListPageServlet(productDao, cartService, recentlyViewedProductsService);

        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("products"), any());
        verify(requestDispatcher).forward(request, response);
    }
}