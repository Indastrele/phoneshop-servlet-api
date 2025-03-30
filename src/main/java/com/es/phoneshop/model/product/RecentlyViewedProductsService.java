package com.es.phoneshop.model.product;

import jakarta.servlet.http.HttpServletRequest;

public interface RecentlyViewedProductsService {
    RecentlyViewedProducts getProducts(HttpServletRequest request);
    void add(RecentlyViewedProducts products, Long id);
}
