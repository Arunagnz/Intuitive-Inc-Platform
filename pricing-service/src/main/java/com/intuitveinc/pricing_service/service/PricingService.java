package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.pricing_service.exception.PricingNotFoundException;
import com.intuitveinc.pricing_service.repository.PricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PricingService implements IPricingService {

    @Autowired
    private PricingRepository pricingRepository;

    @Override
    public List<Pricing> getAllPricing() {
        return pricingRepository.findAll();
    }

    @Override
    public List<Pricing> getPricingByProductId(Long productId) {
        return pricingRepository.findByProductId(productId);
    }

    @Override
    public Pricing getPricingById(Long id) {
        return pricingRepository.findById(id)
                .orElseThrow(() -> new PricingNotFoundException("Pricing with ID " + id + " not found"));
    }

    @Override
    public Pricing createPricing(Pricing pricing) {
        pricing.setCreatedAt(LocalDateTime.now());
        return pricingRepository.save(pricing);
    }

    @Override
    public Pricing updatePricing(Long id, Pricing pricingDetails) {
        Pricing pricing = getPricingById(id);
        pricing.setBasePrice(pricingDetails.getBasePrice());
        pricing.setDiscount(pricingDetails.getDiscount());
        pricing.setPricingStrategy(pricingDetails.getPricingStrategy());
        pricing.setUpdatedAt(LocalDateTime.now());
        return pricingRepository.save(pricing);
    }

    @Override
    public void deletePricing(Long id) {
        pricingRepository.deleteById(id);
    }
}
