package com.es.phoneshop.model.product;

public enum DescriptionSearchType {
    ALL_WORDS, ANY_WORDS;

    public static DescriptionSearchType getType(String name) {
        return switch (name.toLowerCase()) {
            case ("all words") -> ALL_WORDS;
            case ("any words") -> ANY_WORDS;
            default -> null;
        };
    }
}
