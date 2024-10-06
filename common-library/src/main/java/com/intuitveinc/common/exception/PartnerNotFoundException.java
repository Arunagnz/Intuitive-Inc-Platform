package com.intuitveinc.common.exception;

public class PartnerNotFoundException extends RuntimeException {

    public PartnerNotFoundException(String message) {
        super(message);
    }

    public PartnerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
