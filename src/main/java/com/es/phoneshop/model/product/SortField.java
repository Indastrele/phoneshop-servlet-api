package com.es.phoneshop.model.product;

public enum SortField {
    DESCRIPTION, PRICE;

    public static SortField getField(String name) {
        return switch (name.toLowerCase()) {
            case ("description") -> DESCRIPTION;
            case ("price") -> PRICE;
            default -> null;
        };
    }
}
