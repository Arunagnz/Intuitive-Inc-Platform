package com.intuitveinc.pricing_service.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.common.request.DynamicPricingRequest;
import com.intuitveinc.pricing_service.service.IPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/pricing")
public class PricingController {
    private static final Logger logger = LoggerFactory.getLogger(PricingController.class);

    @Autowired
    private IPricingService pricingService;

    @GetMapping
    public ResponseEntity<List<Pricing>> getAllPricing() {
        logger.info("Fetching all pricing");
        List<Pricing> pricing = pricingService.getAllPricing();
        logger.info("Fetched all pricing: {}", pricing);
        return ResponseEntity.ok(pricing);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pricing> getPricingById(@PathVariable Long id) {
        logger.info("Fetching pricing with id: {}", id);
        Pricing pricing = pricingService.getPricingById(id);
        logger.info("Pricing fetched: {}", pricing);
        return ResponseEntity.ok(pricing);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Pricing>> getPricingByProductId(@PathVariable Long productId) {
        logger.info("Fetching pricing with product id: {}", productId);
        List<Pricing> pricingList = pricingService.getPricingByProductId(productId);
        logger.info("Pricing fetched: {}", pricingList);
        return ResponseEntity.ok(pricingList);
    }

    @PostMapping
    public ResponseEntity<Pricing> createPricing(@Valid @RequestBody Pricing pricing) {
        logger.info("Creating pricing: {}", pricing);
        Pricing createdPricing = pricingService.createPricing(pricing);
        logger.info("Pricing created: {}", createdPricing);
        return ResponseEntity.ok(createdPricing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pricing> updatePricing(@PathVariable Long id,@Valid @RequestBody Pricing pricingDetails) {
        logger.info("Updating pricing with id: {}", id);
        Pricing updatedPricing = pricingService.updatePricing(id, pricingDetails);
        logger.info("Pricing updated: {}", updatedPricing);
        return ResponseEntity.ok(updatedPricing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable Long id) {
        logger.info("Deleting pricing with id: {}", id);
        pricingService.deletePricing(id);
        logger.info("Pricing deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dynamic/product/{productId}")
    public ResponseEntity<List<Pricing>> applyDynamicPricing(@PathVariable Long productId,@Valid @RequestBody(required = false) DynamicPricingRequest dynamicPricingRequest) {
        logger.info("Applying dynamic pricing for product with id: {}", productId);
        if (Objects.isNull(dynamicPricingRequest))
            dynamicPricingRequest = new DynamicPricingRequest(0, false);
        
        logger.info("Dynamic pricing request: {}", dynamicPricingRequest);
        List<Pricing> adjustedPrices = pricingService.applyDynamicPricing(productId, dynamicPricingRequest);
        logger.info("Dynamic pricing applied: {}", adjustedPrices);
        return ResponseEntity.ok(adjustedPrices);
    }

    @PostMapping("/promotion/product/{productId}")
    public ResponseEntity<List<Pricing>> applyPromotionPricing(@PathVariable Long productId) {
        logger.info("Applying promotion pricing for product with id: {}", productId);
        List<Pricing> adjustedPrices = pricingService.applyPromotionPricing(productId);
        logger.info("Promotion pricing applied: {}", adjustedPrices);
        return ResponseEntity.ok(adjustedPrices);
    }
}
