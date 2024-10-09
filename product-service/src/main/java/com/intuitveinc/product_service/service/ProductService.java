package com.intuitveinc.product_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.Partner;
import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.common.model.Product;
import com.intuitveinc.common.repository.PartnerRepository;
import com.intuitveinc.product_service.exception.ProductNotFoundException;
import com.intuitveinc.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService implements IProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Value("${pricing.service.url}")
    private String pricingServiceUrl;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    private final WebClient webClient;

    public ProductService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Product createProduct(Product product) {
        logger.info("Creating product: {}", product);
        Long partnerId = product.getPartner().getId();
        logger.info("Fetching partner with ID: {}", partnerId);
        Partner partner = partnerRepository.findById(partnerId)
                        .orElseThrow(() -> new PartnerNotFoundException("Product with ID " + partnerId + " not found"));
        logger.info("Partner fetched: {}", partner);
        product.setPartner(partner);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        logger.info("Product created: {}", product);
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        List<Pricing> pricing = webClient.get()
                .uri(pricingServiceUrl+"/api/v1/pricing/product/" + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Pricing>>() {})
                .block();
        product.setPricing(pricing);
        logger.info("Product fetched: {}", product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        logger.info("Updating product with ID: {}", id);
        Product product = getProductById(id);
        logger.info("Product fetched: {}", product);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setBasePrice(productDetails.getBasePrice());
        product.setUpdatedAt(LocalDateTime.now());
        logger.info("Product updated: {}", product);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
    }
}
