package com.intuitveinc.pricing_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PricingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePricingNotFound(PricingNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),                // Current timestamp
                HttpStatus.NOT_FOUND.value(),       // HTTP status code
                "Pricing Not Found",                // Error title
                ex.getMessage()                     // Error message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
