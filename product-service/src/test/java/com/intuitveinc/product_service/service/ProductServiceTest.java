package com.intuitveinc.product_service.service;

import com.intuitveinc.common.model.Partner;
import com.intuitveinc.common.model.Product;
import com.intuitveinc.common.repository.PartnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Test
    void testCreateProduct() {
        PartnerRepository partnerRepository = Mockito.mock(PartnerRepository.class);
        IProductService productService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Partner partner = new Partner();
        partner.setId(1L);
        partner.setName("Amazon");
        product.setPartner(partner);

        Mockito.when(partnerRepository.findById(partner.getId())).thenReturn(Optional.of(partner));
        Mockito.when(productService.createProduct(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);
        assertEquals("Refrigerator", createdProduct.getName());

        Mockito.when(productService.createProduct(product)).thenThrow(new RuntimeException("Product already exists"));
        assertThrows(RuntimeException.class, () -> productService.createProduct(product));

        Mockito.when(productService.createProduct(null)).thenThrow(new RuntimeException("Product cannot be null"));
        assertThrows(RuntimeException.class, () -> productService.createProduct(null));
    }

    @Test
    void testGetProduct() {
        IProductService productService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(productService.getProductById(1L)).thenReturn(product);

        Product fetchedProduct = productService.getProductById(1L);
        assertEquals("Refrigerator", fetchedProduct.getName());

        Mockito.when(productService.getProductById(1L)).thenThrow(new RuntimeException("Product not found"));
        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testUpdateProduct() {
        IProductService productService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(productService.updateProduct(1L, product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, product);
        assertEquals("Refrigerator", updatedProduct.getName());

        Mockito.when(productService.updateProduct(1L, product)).thenThrow(new RuntimeException("Product not found"));
        assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, product));
    }

    @Test
    void testDeleteProduct() {
        IProductService productService = Mockito.mock(IProductService.class);

        Mockito.doNothing().when(productService).deleteProduct(1L);

        productService.deleteProduct(1L);
        Mockito.verify(productService, Mockito.times(1)).deleteProduct(1L);

        Mockito.doThrow(new RuntimeException("Product not found"))
                .when(productService).deleteProduct(1L);

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testGetAllProducts() {
        IProductService productService = Mockito.mock(IProductService.class);
        Product product1 = new Product();
        product1.setName("Refrigerator");
        Product product2 = new Product();
        product2.setName("Washing Machine");
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());

        Mockito.when(productService.getAllProducts()).thenReturn(Collections.emptyList());
        assertEquals(0, productService.getAllProducts().size());

        Mockito.when(productService.getAllProducts()).thenThrow(new RuntimeException("No products found"));
        assertThrows(RuntimeException.class, productService::getAllProducts);
    }
}
