package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.Partner;
import com.intuitveinc.common.model.Product;
import com.intuitveinc.common.model.Subscription;
import com.intuitveinc.common.repository.PartnerRepository;
import com.intuitveinc.common.repository.SubscriptionRepository;
import com.intuitveinc.pricing_service.exception.SubscriptionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService implements ISubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    private WebClient webClient;

    public SubscriptionService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        logger.info("Creating subscription: {}", subscription);

        Long partnerId = subscription.getPartner().getId();
        logger.info("Fetching partner with ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerNotFoundException("Product with ID " + partnerId + " not found"));
        logger.info("Partner fetched: {}", partner);
        subscription.setPartner(partner);

        Long productId = subscription.getProduct().getId();
        Product product = webClient.get()
                .uri(productServiceUrl + "/api/products/" + productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Product with ID " + productId + " not found")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Downstream unreachable, Try again in sometime")))
                .bodyToMono(Product.class)
                .block();
        logger.info("Product fetched: {}", product);
        subscription.setProduct(product);
        subscription.setCreatedAt(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());
        logger.info("Subscription created: {}", subscription);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionById(Long id) {
        logger.info("Fetching subscription with id: {}", id);
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription with ID " + id + " not found"));
    }

    @Override
    public List<Subscription> getSubscriptionByProductId(Long productId) {
        logger.info("Fetching subscription with product id: {}", productId);
        return subscriptionRepository.findByProductId(productId);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        logger.info("Fetching all subscriptions");
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription updateSubscription(Long id, Subscription subscriptionDetails) {
        logger.info("Updating subscription with ID: {}", id);
        Subscription subscription = getSubscriptionById(id);
        logger.info("Subscription fetched: {}", subscription);
        subscription.setSoldPrice(subscriptionDetails.getSoldPrice());
        subscription.setStartDate(subscriptionDetails.getStartDate());
        subscription.setEndDate(subscriptionDetails.getEndDate());
        subscription.setUpdatedAt(LocalDateTime.now());
        logger.info("Subscription updated: {}", subscription);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        logger.info("Deleting subscription with ID: {}", id);
        subscriptionRepository.deleteById(id);
    }
}
