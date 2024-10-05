package com.intuitveinc.product_service.service;

import com.intuitveinc.common.model.Pricing;
import com.intuitveinc.common.model.Product;
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

    @Value("${pricing.service.url}")
    private String pricingServiceUrl;

    @Autowired
    private ProductRepository productRepository;

    private final WebClient webClient;

    public ProductService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        List<Pricing> pricing = webClient.get()
                .uri(pricingServiceUrl+"/api/v1/pricing/product/" + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Pricing>>() {})
                .block();
        product.setPricing(pricing);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setBasePrice(productDetails.getBasePrice());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
