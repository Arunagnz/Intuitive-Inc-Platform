package com.intuitveinc.common.validator;

import com.intuitveinc.common.model.Promotion;
import com.intuitveinc.common.model.Subscription;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if(object instanceof Subscription subscription) {
            return isValidDateRange(subscription.getStartDate(), subscription.getEndDate());
        }
        else if(object instanceof Promotion promotion) {
            return isValidDateRange(promotion.getStartDate(), promotion.getEndDate());
        }
        return true;
    }

    private boolean isValidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return endDate.isAfter(LocalDateTime.now()) && endDate.isAfter(startDate);
    }
}
