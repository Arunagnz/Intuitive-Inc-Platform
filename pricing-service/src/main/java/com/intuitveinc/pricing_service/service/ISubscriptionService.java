package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.model.Subscription;

import java.util.List;

public interface ISubscriptionService {
    Subscription createSubscription(Subscription subscription);
    Subscription getSubscriptionById(Long id);
    List<Subscription> getAllSubscriptions();
    List<Subscription> getSubscriptionByProductId(Long productId);
    Subscription updateSubscription(Long id, Subscription subscriptionDetails);
    void deleteSubscription(Long id);
}
