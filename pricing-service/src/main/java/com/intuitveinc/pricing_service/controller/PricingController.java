package com.intuitveinc.pricing_service.controller;

import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.common.request.DynamicPricingRequest;
import com.intuitveinc.pricing_service.service.IPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/pricing")
public class PricingController {

    @Autowired
    private IPricingService pricingService;

    @GetMapping
    public ResponseEntity<List<Pricing>> getAllPricing() {
        List<Pricing> pricing = pricingService.getAllPricing();
        return ResponseEntity.ok(pricing);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pricing> getPricingById(@PathVariable Long id) {
        Pricing pricing = pricingService.getPricingById(id);
        return ResponseEntity.ok(pricing);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Pricing>> getPricingByProductId(@PathVariable Long productId) {
        List<Pricing> pricingList = pricingService.getPricingByProductId(productId);
        return ResponseEntity.ok(pricingList);
    }

    @PostMapping
    public ResponseEntity<Pricing> createPricing(@RequestBody Pricing pricing) {
        Pricing createdPricing = pricingService.createPricing(pricing);
        return ResponseEntity.ok(createdPricing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pricing> updatePricing(@PathVariable Long id, @RequestBody Pricing pricingDetails) {
        Pricing updatedPricing = pricingService.updatePricing(id, pricingDetails);
        return ResponseEntity.ok(updatedPricing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable Long id) {
        pricingService.deletePricing(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dynamic/product/{productId}")
    public ResponseEntity<List<Pricing>> applyDynamicPricing(@PathVariable Long productId, @RequestBody(required = false) DynamicPricingRequest dynamicPricingRequest) {
        if (Objects.isNull(dynamicPricingRequest))
            dynamicPricingRequest = new DynamicPricingRequest(0, false);

        List<Pricing> adjustedPrices = pricingService.applyDynamicPricing(productId, dynamicPricingRequest);
        return ResponseEntity.ok(adjustedPrices);
    }
}
