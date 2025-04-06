package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
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
    @Mock
    private Product product;

    private ProductDetailsPageServlet servlet;

    @Before
    public void setup() throws ServletException {
        when(productDao.getProduct(1L)).thenReturn(product);
        when(productDao.getProduct(11L)).thenThrow(new ProductNotFoundException(11L));
        when(product.getId()).thenReturn(1L);
        when(recentlyViewedProductsService.getProducts(request)).thenReturn(recentlyViewedProducts);
        when(cartService.getCart(request)).thenReturn(cart);

        servlet = new ProductDetailsPageServlet(productDao, cartService, recentlyViewedProductsService);
        servlet.init(config);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.US);
    }

    @Test
    public void testDoGetWithData() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("recentlyViewed"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDoGetWithOutData() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/11");
        servlet.doGet(request, response);
    }

    @Test
    public void testPost() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("quantity")).thenReturn("1");
        when(request.getContextPath()).thenReturn("/WEB-INF");
        servlet.doPost(request, response);

        verify(response).sendRedirect("/WEB-INF/products/1?message=Successfully added to cart");
    }

    @Test(expected = NumberFormatException.class)
    public void testPostWithPathInfoError() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1a");
        servlet.doPost(request, response);
    }

    @Test
    public void testPostWithQuantityParameterCastError() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("quantity")).thenReturn("@");
        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("recentlyViewed"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testPostWithNotEnoughStockCastError() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getParameter("quantity")).thenReturn("100");
        doThrow(new NotEnoughStockException(1L, 0, 1)).when(cartService).add(cart, 1L, 100);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), any());
        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("recentlyViewed"), any());
        verify(requestDispatcher).forward(request, response);
    }
}