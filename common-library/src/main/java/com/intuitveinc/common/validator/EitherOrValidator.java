package com.intuitveinc.common.validator;

import com.intuitveinc.common.model.Promotion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EitherOrValidator implements ConstraintValidator<EitherOr, Promotion> {

    @Override
    public void initialize(EitherOr constraintAnnotation) {
    }

    @Override
    public boolean isValid(Promotion promotion, ConstraintValidatorContext context) {
        return (promotion.getPercentage() != null && promotion.getFlatRate() == null) ||
                (promotion.getPercentage() == null && promotion.getFlatRate() != null);
    }
}
