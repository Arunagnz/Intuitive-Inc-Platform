package com.intuitveinc.pricing_service.controller;

import com.intuitveinc.common.model.Subscription;
import com.intuitveinc.pricing_service.service.ISubscriptionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private ISubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscription() {
        logger.info("Fetching all subscription");
        List<Subscription> subscription = subscriptionService.getAllSubscriptions();
        logger.info("Fetched all subscription: {}", subscription);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        logger.info("Fetching subscription with id: {}", id);
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        logger.info("Subscription fetched: {}", subscription);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Subscription>> getSubscriptionByProductId(@PathVariable Long productId) {
        logger.info("Fetching subscription with product id: {}", productId);
        List<Subscription> subscriptionList = subscriptionService.getSubscriptionByProductId(productId);
        logger.info("Subscription fetched: {}", subscriptionList);
        return ResponseEntity.ok(subscriptionList);
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@Valid @RequestBody Subscription subscription) {
        logger.info("Creating subscription: {}", subscription);
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        logger.info("Subscription created: {}", createdSubscription);
        return ResponseEntity.ok(createdSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id,@Valid @RequestBody Subscription subscriptionDetails) {
        logger.info("Updating subscription with id: {}", id);
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDetails);
        logger.info("Subscription updated: {}", updatedSubscription);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        logger.info("Deleting subscription with id: {}", id);
        subscriptionService.deleteSubscription(id);
        logger.info("Subscription deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
