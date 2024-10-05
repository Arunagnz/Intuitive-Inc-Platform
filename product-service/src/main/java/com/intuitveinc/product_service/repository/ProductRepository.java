package com.intuitveinc.product_service.repository;

import com.intuitveinc.common.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods if needed
}
