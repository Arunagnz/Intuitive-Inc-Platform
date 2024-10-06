package com.intuitveinc.pricing_service;

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

	public static void main(String[] args) {
		SpringApplication.run(PricingServiceApplication.class, args);
	}

}
