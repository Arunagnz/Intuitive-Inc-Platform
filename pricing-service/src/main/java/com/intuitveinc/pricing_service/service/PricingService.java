package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.*;
import com.intuitveinc.common.repository.PartnerRepository;
import com.intuitveinc.common.repository.PromotionRepository;
import com.intuitveinc.common.request.DynamicPricingRequest;
import com.intuitveinc.common.strategy.DynamicPricingStrategy;
import com.intuitveinc.common.strategy.MonthlyPricingStrategy;
import com.intuitveinc.common.strategy.VolumeBasedPricingStrategy;
import com.intuitveinc.common.strategy.YearlyPricingStrategy;
import com.intuitveinc.pricing_service.exception.PricingNotFoundException;
import com.intuitveinc.pricing_service.metric.PricingMetrics;
import com.intuitveinc.pricing_service.repository.PricingRepository;
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
public class PricingService implements IPricingService {
    private static final Logger logger = LoggerFactory.getLogger(PricingService.class);

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PricingMetrics pricingMetrics;

    private WebClient webClient;

    public PricingService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public List<Pricing> getAllPricing() {
        logger.info("Fetching all pricing");
        return pricingRepository.findAll();
    }

    @Override
    public List<Pricing> getPricingByProductId(Long productId) {
        logger.info("Fetching pricing with product id: {}", productId);
        List<Pricing> pricingList = pricingRepository.findByProductId(productId);
        for(Pricing pricing : pricingList) {
            double finalPrice = calculateFinalPrice(pricing);
            logger.info("Final price calculated for pricing: {} Base Price: {} Final Price: {}", pricing.getId(), pricing.getBasePrice(), finalPrice);
            pricing.setBasePrice(finalPrice);
        }
        return pricingList;
    }

    @Override
    public Pricing getPricingById(Long id) {
        logger.info("Fetching pricing with id: {}", id);
        Pricing pricing = pricingRepository.findById(id)
                .orElseThrow(() -> new PricingNotFoundException("Pricing with ID " + id + " not found"));
        double finalPrice = calculateFinalPrice(pricing);
        logger.info("Final price calculated for pricing: {} Base Price: {} Final Price: {}", pricing.getId(), pricing.getBasePrice(), finalPrice);
        pricing.setBasePrice(finalPrice);
        return pricing;
    }

    @Override
    public Pricing createPricing(Pricing pricing) {
        logger.info("Creating pricing: {}", pricing);
        pricing.setCreatedAt(LocalDateTime.now());
        Long partnerId = pricing.getPartner().getId();
        logger.info("Fetching partner with ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerNotFoundException("Partner with ID " + partnerId + " not found"));
        logger.info("Partner fetched: {}", partner);
        pricing.setPartner(partner);
        Long productId = pricing.getProduct().getId();
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
        pricing.setProduct(product);
        return pricingRepository.save(pricing);
    }

    @Override
    public Pricing updatePricing(Long id, Pricing pricingDetails) {
        logger.info("Updating pricing with id: {}", id);
        Pricing pricing = getPricingById(id);
        pricing.setBasePrice(pricingDetails.getBasePrice());
        pricing.setDiscount(pricingDetails.getDiscount());
        pricing.setPricingStrategy(pricingDetails.getPricingStrategy());
        pricing.setUpdatedAt(LocalDateTime.now());
        logger.info("Pricing updated: {}", pricing);
        return pricingRepository.save(pricing);
    }

    @Override
    public void deletePricing(Long id) {
        logger.info("Deleting pricing with id: {}", id);
        pricingRepository.deleteById(id);
        logger.info("Pricing deleted with id: {}", id);
    }

    public double calculateFinalPrice(Pricing pricing) {
        PricingStrategy strategy = pricing.getPricingStrategy();
        logger.info("Calculating final price for pricing: {} Strategy: {}", pricing.getId(), strategy);
        return switch (strategy) {
            case MONTHLY -> new MonthlyPricingStrategy().calculatePrice(pricing.getBasePrice(), pricing.getDiscount());
            case YEARLY -> new YearlyPricingStrategy().calculatePrice(pricing.getBasePrice(), pricing.getDiscount());
            case VOLUME_BASED ->
                    new VolumeBasedPricingStrategy().calculatePrice(pricing.getBasePrice(), pricing.getDiscount());
            case DYNAMIC -> new DynamicPricingStrategy().calculatePrice(pricing.getBasePrice(), pricing.getDiscount());
            default -> pricing.getBasePrice(); // Fallback to base price
        };
    }

    public List<Pricing> applyDynamicPricing(Long productId, DynamicPricingRequest dynamicPricingRequest) {
        logger.info("Starting dynamic pricing calculation for product: {}", productId);
        List<Pricing> pricingList = pricingRepository.findByProductId(productId);

        double priceAdjustmentFactor = 1.0;

        // Simple logic: If demand is greater than supply or Partner override, increase price by 10% or given %
        if (dynamicPricingRequest.isOverrideDemand() || isHighDemand()) {
            if (dynamicPricingRequest.getPercentage() > 0)
                priceAdjustmentFactor += dynamicPricingRequest.getPercentage() / 100;
            else
                priceAdjustmentFactor = 1.10;
        }

        logger.info("Price adjustment factor: {}", priceAdjustmentFactor);
        for (Pricing pricing : pricingList) {
            double finalPrice = calculateFinalPrice(pricing) * priceAdjustmentFactor;
            pricing.setBasePrice(finalPrice);
            pricing.setUpdatedAt(LocalDateTime.now());
            logger.info("Price adjusted due to demand for pricing: {} Base Price: {} Adjusted Price: {}", pricing.getId(), pricing.getBasePrice(), finalPrice);
        }
        logger.info("Dynamic pricing applied for product: {}", productId);
        pricingMetrics.recordDynamicPriceAdjustment();
        return pricingList;
    }

    public List<Pricing> applyPromotionPricing(Long productId) {
        logger.info("Starting promotion pricing calculation for product: {}", productId);
        List<Pricing> pricingList = pricingRepository.findByProductId(productId);

        if(pricingList.isEmpty()) {
            logger.info("No pricing found for the product: {}", productId);
            return pricingList;
        }

        Long partnerId = pricingList.getFirst().getPartner().getId();
        List<Promotion> promotions = promotionRepository.findByPartnerIdAndProductId(partnerId, productId, LocalDateTime.now());

        if(promotions.isEmpty()) {
            logger.info("No promotions found for the product: {}", productId);
            return pricingList;
        }

        for (Pricing pricing : pricingList) {
            double basePrice = calculateFinalPrice(pricing);
            for (Promotion promotion : promotions) {
                double discount = 0.0;
                if (promotion.getPercentage() > 0) {
                    discount = basePrice * (promotion.getPercentage() / 100);
                }
                if (promotion.getFlatRate() > 0 && discount > promotion.getFlatRate()) {
                    discount = promotion.getFlatRate();
                }
                basePrice -= discount;
            }
            pricing.setBasePrice(basePrice);
        }
        return pricingList;
    }

    private boolean isHighDemand() {
        // Simulate supply and demand factors (hardcoded for now)
        double supply = 50;  // e.g., current stock level -> Inventory service call
        double demand = 100; // e.g., recent customer interest -> Customer activity tracking

        return demand > supply;
    }
}
