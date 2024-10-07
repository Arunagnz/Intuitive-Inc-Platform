package com.intuitveinc.product_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.intuitveinc.product_service.model",
		"com.intuitveinc.common.model"
})
@EnableJpaRepositories(basePackages = {
		"com.intuitveinc.product_service.repository",
		"com.intuitveinc.common.repository"
})
public class ProductServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Product Service Application");
		SpringApplication.run(ProductServiceApplication.class, args);
		logger.info("Product Service Application Started");
	}
}
