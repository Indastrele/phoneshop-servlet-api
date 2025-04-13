package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<CartItem> itemList;
    private int totalQuantity;
    private BigDecimal totalPrice;

    public Cart() {
        this.itemList = new ArrayList<>();
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setItemList(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
