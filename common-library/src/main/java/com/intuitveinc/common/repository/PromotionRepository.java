package com.intuitveinc.common.repository;

import com.intuitveinc.common.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    // Custom queries can be added here if needed
    @Query("SELECT p FROM Promotion p WHERE p.partner.id = :partnerId AND (:productId IS NULL OR p.product.id = :productId) AND :currentTime BETWEEN p.startDate AND p.endDate")
    List<Promotion> findByPartnerIdAndProductId(@Param("partnerId") Long partnerId, @Param("productId") Long productId, @Param("currentTime") LocalDateTime currentTime);

    List<Promotion> findByProductId(Long productId);
}
