package com.intuitveinc.pricing_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.intuitveinc.pricing_service.model",
		"com.intuitveinc.common.model"
})
@EnableJpaRepositories(basePackages = {
		"com.intuitveinc.pricing_service.repository",
		"com.intuitveinc.common.repository"
})
public class PricingServiceApplication {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PricingServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Pricing Service Application");
		SpringApplication.run(PricingServiceApplication.class, args);
		logger.info("Pricing Service Application Started");
	}

}
