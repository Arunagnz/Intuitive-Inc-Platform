package com.intuitveinc.common.strategy;

public class VolumeBasedPricingStrategy extends BasePricingStrategy {
    public double calculatePrice(double basePrice, double discount) {
        double price = basePrice - discount;

        boolean isVolumeThresholdReached = true;
        if (isVolumeThresholdReached) {
            price += price * 0.10;
        }

        return price;
    }
}
