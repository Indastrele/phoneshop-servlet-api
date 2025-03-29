package com.es.phoneshop.model.product;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder getOrder(String name) {
        return switch (name.toLowerCase()) {
            case ("asc") -> ASC;
            case ("desc") -> DESC;
            default -> null;
        };
    }
}
