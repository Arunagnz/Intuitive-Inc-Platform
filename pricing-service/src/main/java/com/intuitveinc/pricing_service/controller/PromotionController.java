package com.intuitveinc.pricing_service.controller;

import com.intuitveinc.common.model.Promotion;
import com.intuitveinc.pricing_service.service.IPromotionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotion")
public class PromotionController {
    private static final Logger logger = LoggerFactory.getLogger(PromotionController.class);

    @Autowired
    private IPromotionService promotionService;

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotion() {
        logger.info("Fetching all promotion");
        List<Promotion> promotion = promotionService.getAllPromotions();
        logger.info("Fetched all promotion: {}", promotion);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        logger.info("Fetching promotion with id: {}", id);
        Promotion promotion = promotionService.getPromotionById(id);
        logger.info("Promotion fetched: {}", promotion);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Promotion>> getPromotionByProductId(@PathVariable Long productId) {
        logger.info("Fetching promotion with product id: {}", productId);
        List<Promotion> promotionList = promotionService.getPromotionByProductId(productId);
        logger.info("Promotion fetched: {}", promotionList);
        return ResponseEntity.ok(promotionList);
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@Valid @RequestBody Promotion promotion) {
        logger.info("Creating promotion: {}", promotion);
        Promotion createdPromotion = promotionService.createPromotion(promotion);
        logger.info("Promotion created: {}", createdPromotion);
        return ResponseEntity.ok(createdPromotion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id,@Valid @RequestBody Promotion promotionDetails) {
        logger.info("Updating promotion with id: {}", id);
        Promotion updatedPromotion = promotionService.updatePromotion(id, promotionDetails);
        logger.info("Promotion updated: {}", updatedPromotion);
        return ResponseEntity.ok(updatedPromotion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        logger.info("Deleting promotion with id: {}", id);
        promotionService.deletePromotion(id);
        logger.info("Promotion deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
