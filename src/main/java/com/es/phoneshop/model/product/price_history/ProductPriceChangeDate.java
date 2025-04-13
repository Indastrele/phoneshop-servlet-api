package com.es.phoneshop.model.product.price_history;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductPriceChangeDate implements Serializable {
    private final BigDecimal price;

    private final LocalDate date;

    public ProductPriceChangeDate(BigDecimal price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public LocalDate getDate() {
        return this.date;
    }
}
