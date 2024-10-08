package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.common.request.DynamicPricingRequest;

import java.util.List;

public interface IPricingService {
    Pricing createPricing(Pricing pricing);
    Pricing getPricingById(Long Id);
    List<Pricing> getAllPricing();
    List<Pricing> getPricingByProductId(Long productId);
    Pricing updatePricing(Long id, Pricing pricing);
    void deletePricing(Long id);
    List<Pricing> applyDynamicPricing(Long productId, DynamicPricingRequest dynamicPricingRequest);
    List<Pricing> applyPromotionPricing(Long productId);
}
