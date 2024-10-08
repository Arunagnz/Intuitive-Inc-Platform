package com.intuitveinc.pricing_service.exception;

public class PromotionNotFoundException extends RuntimeException {

    public PromotionNotFoundException(String message) {
        super(message);
    }

    public PromotionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
