package com.intuitveinc.common.repository;

import com.intuitveinc.common.model.Promotion;
import com.intuitveinc.common.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Custom queries can be added here if needed
    List<Subscription> findByProductId(Long productId);
}
