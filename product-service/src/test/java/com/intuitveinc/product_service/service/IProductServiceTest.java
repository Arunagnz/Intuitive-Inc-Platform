package com.intuitveinc.product_service.service;

import com.intuitveinc.common.model.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IProductServiceTest {

    @Test
    void testCreateProduct() {
        IProductService IProductService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(IProductService.createProduct(product)).thenReturn(product);

        Product createdProduct = IProductService.createProduct(product);
        System.out.println(createdProduct);
        assertEquals("Refrigerator", createdProduct.getName());
    }
}
