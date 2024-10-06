package com.intuitveinc.pricing_service.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PricingMetrics {

    private final Counter dynamicPriceAdjustments;

    public PricingMetrics(MeterRegistry registry) {
        this.dynamicPriceAdjustments = Counter.builder("pricing.dynamic.adjustments")
                .description("The number of dynamic price adjustments made")
                .register(registry);
    }

    public void recordDynamicPriceAdjustment() {
        dynamicPriceAdjustments.increment();
    }
}
