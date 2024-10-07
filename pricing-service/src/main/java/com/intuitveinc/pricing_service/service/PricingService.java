package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.Partner;
import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.common.model.PricingStrategy;
import com.intuitveinc.common.model.Product;
import com.intuitveinc.common.repository.PartnerRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
        return pricingRepository.findByProductId(productId);
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
                .orElseThrow(() -> new PartnerNotFoundException("Product with ID " + partnerId + " not found"));
        logger.info("Partner fetched: {}", partner);
        pricing.setPartner(partner);
        Product product = webClient.get()
                .uri(productServiceUrl + "/api/products/" + pricing.getProduct().getId())
                .retrieve()
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

        // Simulate supply and demand factors (hardcoded for now)
        double supply = 50;  // e.g., current stock level -> Inventory service call
        double demand = 100; // e.g., recent customer interest -> Customer activity tracking

        double priceAdjustmentFactor = 1.0;

        // Simple logic: If demand is greater than supply or Partner override, increase price by 5% or given %
        if (dynamicPricingRequest.isOverrideDemand() || demand > supply) {
            if (dynamicPricingRequest.getPercentage() > 0)
                priceAdjustmentFactor += dynamicPricingRequest.getPercentage() / 100;
            else
                priceAdjustmentFactor = 1.05;
        }

        logger.info("Price adjustment factor: {}", priceAdjustmentFactor);
        for (Pricing pricing : pricingList) {
            double finalPrice = pricing.getBasePrice() * priceAdjustmentFactor;
            pricing.setBasePrice(finalPrice);
            pricing.setUpdatedAt(LocalDateTime.now());
            logger.info("Price adjusted due to demand for pricing: {} Base Price: {} Adjusted Price: {}", pricing.getId(), pricing.getBasePrice(), finalPrice);
        }
        pricingRepository.saveAll(pricingList);
        logger.info("Dynamic pricing applied for product: {}", productId);
        pricingMetrics.recordDynamicPriceAdjustment();
        return pricingList;
    }
}
