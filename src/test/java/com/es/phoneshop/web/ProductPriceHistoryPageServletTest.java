package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.model.product.RecentlyViewedProductsService;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryPageServletTest {
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
    private RecentlyViewedProductsService recentlyViewedProductsService;
    @Mock
    private RecentlyViewedProducts recentlyViewedProducts;
    @Mock
    private Product product;
    private ProductPriceHistoryPageServlet servlet;

    @Before
    public void setup() throws ServletException {
        when(productDao.getProduct(1L)).thenReturn(product);
        when(productDao.getProduct(11L)).thenThrow(new ProductNotFoundException(11L));
        when(recentlyViewedProductsService.getProducts(request)).thenReturn(recentlyViewedProducts);

        servlet = new ProductPriceHistoryPageServlet(productDao, recentlyViewedProductsService);
        servlet.init(config);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGetWithData() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = NumberFormatException.class)
    public void testPostWithPathInfoError() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1a");
        servlet.doGet(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDoGetWithOutData() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/11");
        servlet.doGet(request, response);
    }
}