package com.intuitveinc.product_service.service;

import com.intuitveinc.common.model.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
}
