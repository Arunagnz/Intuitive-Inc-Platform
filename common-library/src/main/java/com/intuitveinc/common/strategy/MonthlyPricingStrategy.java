package com.intuitveinc.common.strategy;

public class MonthlyPricingStrategy implements PricingStrategy {
    public double calculatePrice(double basePrice, double discount) {
        return basePrice - discount;
    }
}
