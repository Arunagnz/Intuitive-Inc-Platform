package com.intuitveinc.common.exception;

public class PartnerAuthenticationException extends RuntimeException {

    public PartnerAuthenticationException(String message) {
        super(message);
    }

    public PartnerAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}