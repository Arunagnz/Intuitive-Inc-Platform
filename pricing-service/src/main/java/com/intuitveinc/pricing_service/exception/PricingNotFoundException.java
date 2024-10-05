package com.intuitveinc.pricing_service.exception;

public class PricingNotFoundException extends RuntimeException {

    public PricingNotFoundException(String message) {
        super(message);
    }

    public PricingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
