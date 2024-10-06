package com.intuitveinc.common.strategy;

public abstract class BasePricingStrategy implements PricingStrategy {
    protected double applyMinimumPrice(double calculatedPrice, double minPrice) {
        return Math.max(calculatedPrice, minPrice);
    }
}
