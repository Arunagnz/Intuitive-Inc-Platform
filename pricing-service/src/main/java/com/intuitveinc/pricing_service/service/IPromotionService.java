package com.intuitveinc.pricing_service.service;

import com.intuitveinc.common.model.Promotion;

import java.util.List;

public interface IPromotionService {
    Promotion createPromotion(Promotion promotion);
    Promotion getPromotionById(Long id);
    List<Promotion> getPromotionByProductId(Long productId);
    List<Promotion> getAllPromotions();
    Promotion updatePromotion(Long id, Promotion promotionDetails);
    void deletePromotion(Long id);
}
