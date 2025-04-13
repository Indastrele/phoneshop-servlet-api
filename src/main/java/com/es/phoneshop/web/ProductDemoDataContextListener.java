package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.price_history.ProductPriceChangeDate;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ProductDemoDataContextListener implements ServletContextListener {
    private final ProductDao dao;

    public ProductDemoDataContextListener() {
        this.dao = ArrayListProductDao.getInstance();
    }

    public ProductDemoDataContextListener(ProductDao dao) {
        this.dao = dao;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        boolean insertDemoData = Boolean.parseBoolean(sce.getServletContext().getInitParameter("insertDemoData"));
        if (insertDemoData) {
            getSampleProductList().forEach(product -> dao.save(product));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private List<Product> getSampleProductList() {
        final Currency usd = Currency.getInstance("USD");
        List<Product> result = new ArrayList<>();

        // Докинуть дополнительных дат для цен
        result.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(100), LocalDate.now()), new ProductPriceChangeDate(new BigDecimal(110), LocalDate.now().minusDays(2)))));
        result.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(200), LocalDate.now()))));
        result.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(300), LocalDate.now()))));
        result.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(200), LocalDate.now()))));
        result.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(1000), LocalDate.now()))));
        result.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(320), LocalDate.now()))));
        result.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(420), LocalDate.now()))));
        result.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(120), LocalDate.now()))));
        result.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(70), LocalDate.now()))));
        result.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(170), LocalDate.now()))));
        result.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(70), LocalDate.now()))));
        result.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(80), LocalDate.now()))));
        result.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", List.of(new ProductPriceChangeDate(new BigDecimal(150), LocalDate.now()))));

        return result;
    }
}
