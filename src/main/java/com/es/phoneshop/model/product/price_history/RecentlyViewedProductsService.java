package com.es.phoneshop.model.product.price_history;

import jakarta.servlet.http.HttpServletRequest;

public interface RecentlyViewedProductsService {
    RecentlyViewedProducts getProducts(HttpServletRequest request);
    void add(RecentlyViewedProducts products, Long id);
}
