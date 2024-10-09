package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.Partner;
import com.intuitveinc.common.model.Product;
import com.intuitveinc.common.model.Promotion;
import com.intuitveinc.common.repository.PartnerRepository;
import com.intuitveinc.common.repository.PromotionRepository;
import com.intuitveinc.pricing_service.exception.PromotionNotFoundException;
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
public class PromotionService implements IPromotionService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionService.class);

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    private WebClient webClient;

    public PromotionService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Promotion createPromotion(Promotion promotion) {
        logger.info("Creating promotion: {}", promotion);

        // Fetch Partner asynchronously from Optional
        Long partnerId = promotion.getPartner().getId();
        logger.info("Fetching partner with ID: {}", partnerId);

        Mono<Partner> partnerMono = Mono.justOrEmpty(partnerRepository.findById(partnerId))
                .switchIfEmpty(Mono.error(new PartnerNotFoundException("Partner with ID " + partnerId + " not found")));

        // Fetch Product asynchronously using WebClient
        Long productId = promotion.getProduct().getId();
        Mono<Product> productMono = webClient.get()
                .uri(productServiceUrl + "/api/products/" + productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Product with ID " + productId + " not found"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Downstream unreachable, try again later"))
                )
                .bodyToMono(Product.class);

        // Combine both partnerMono and productMono to ensure both complete before continuing
        return Mono.zip(partnerMono, productMono)
                .flatMap(tuple -> {
                    Partner partner = tuple.getT1();
                    Product product = tuple.getT2();

                    // Set the partner and product on the promotion
                    promotion.setPartner(partner);
                    promotion.setProduct(product);
                    promotion.setCreatedAt(LocalDateTime.now());
                    promotion.setUpdatedAt(LocalDateTime.now());

                    logger.info("Promotion created: {}", promotion);

                    // Save promotion asynchronously
                    return Mono.just(promotionRepository.save(promotion));
                })
                .doOnError(error -> logger.error("Error occurred while creating promotion: {}", error.getMessage()))
                .doOnSuccess(savedPromotion -> logger.info("Promotion successfully saved: {}", savedPromotion))
                .block();
    }


    @Override
    public Promotion getPromotionById(Long id) {
        logger.info("Fetching promotion with id: {}", id);
        return promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionNotFoundException("Promotion with ID " + id + " not found"));
    }

    @Override
    public List<Promotion> getPromotionByProductId(Long productId) {
        logger.info("Fetching promotion with product id: {}", productId);
        return promotionRepository.findByProductId(productId);
    }

    @Override
    public List<Promotion> getAllPromotions() {
        logger.info("Fetching all promotions");
        return promotionRepository.findAll();
    }

    @Override
    public Promotion updatePromotion(Long id, Promotion promotionDetails) {
        logger.info("Updating promotion with ID: {}", id);
        Promotion promotion = getPromotionById(id);
        logger.info("Promotion fetched: {}", promotion);
        promotion.setDescription(promotionDetails.getDescription());
        promotion.setFlatRate(promotionDetails.getFlatRate());
        promotion.setPercentage(promotionDetails.getPercentage());
        promotion.setStartDate(promotionDetails.getStartDate());
        promotion.setEndDate(promotionDetails.getEndDate());
        promotion.setUpdatedAt(LocalDateTime.now());
        logger.info("Promotion updated: {}", promotion);
        return promotionRepository.save(promotion);
    }

    @Override
    public void deletePromotion(Long id) {
        logger.info("Deleting promotion with ID: {}", id);
        promotionRepository.deleteById(id);
    }
}
