package com.intuitveinc.common.strategy;

public interface PricingStrategy {
    double calculatePrice(double basePrice, double discount);
}
