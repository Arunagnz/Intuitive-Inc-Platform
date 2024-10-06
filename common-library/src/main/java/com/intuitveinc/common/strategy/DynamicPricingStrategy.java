package com.intuitveinc.common.strategy;

public class DynamicPricingStrategy extends BasePricingStrategy {
    public double calculatePrice(double basePrice, double discount) {
        double price = basePrice - discount;

        boolean highDemand = true;
        if (highDemand) {
            price += price * 0.05;
        }

        return price;
    }
}
