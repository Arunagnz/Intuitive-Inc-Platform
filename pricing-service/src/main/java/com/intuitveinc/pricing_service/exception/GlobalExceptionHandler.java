package com.intuitveinc.pricing_service.exception;

import com.intuitveinc.common.exception.PartnerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PricingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePricingNotFound(PricingNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),                // Current timestamp
                HttpStatus.NOT_FOUND.value(),       // HTTP status code
                "Pricing Not Found",                // Error title
                ex.getMessage()                     // Error message: Only for debug purpose
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PartnerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePartnerNotFound(PartnerNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),                // Current timestamp
                HttpStatus.NOT_FOUND.value(),       // HTTP status code
                "Partner Not Found",                // Error title
                ex.getMessage()                     // Error message: Only for debug purpose
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),                            // Current timestamp
                HttpStatus.INTERNAL_SERVER_ERROR.value(),       // HTTP status code
                "Server unreachable at the moment",             // Error title
                ex.getMessage()                                 // Error message: Only for debug purpose
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                details.toString(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
