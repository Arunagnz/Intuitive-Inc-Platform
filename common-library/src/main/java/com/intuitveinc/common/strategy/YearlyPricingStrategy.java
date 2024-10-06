package com.intuitveinc.common.strategy;

public class YearlyPricingStrategy implements PricingStrategy {
    public double calculatePrice(double basePrice, double discount) {
        return (basePrice - discount) * 12; // Example logic
    }
}
