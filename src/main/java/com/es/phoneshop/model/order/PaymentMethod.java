package com.es.phoneshop.model.order;

public enum PaymentMethod {
    CASH, CREDIT_CARD;

    public static PaymentMethod getMethodOf(String methodName) {
        var loverName = methodName.toLowerCase();
        return switch (loverName) {
            case "cash" -> CASH;
            case "credit card", "credit_card" -> CREDIT_CARD;
            default -> null;
        };
    }

}
