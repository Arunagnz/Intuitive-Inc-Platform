package com.intuitveinc.common.repository;

import com.intuitveinc.common.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    // Custom queries can be added here if needed
}
