package com.intuitveinc.pricing_service.repository;

import com.intuitveinc.common.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom queries can be added here if needed
    List<Pricing> findByProductId(Long productId);
}
