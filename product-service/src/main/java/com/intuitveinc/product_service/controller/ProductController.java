package com.intuitveinc.product_service.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.intuitveinc.common.model.Product;
import com.intuitveinc.product_service.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        logger.info("Creating product: {}", product);
        Product createdProduct = productService.createProduct(product);
        logger.info("Product created: {}", createdProduct);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with id: {}", id);
        Product product = productService.getProductById(id);
        logger.info("Product fetched: {}", product);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productService.getAllProducts();
        logger.info("Fetched all products: {}", products);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@Valid @RequestBody Product productDetails) {
        logger.info("Updating product with id: {}", id);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        logger.info("Product updated: {}", updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        logger.info("Product deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
