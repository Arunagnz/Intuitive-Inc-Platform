package com.intuitveinc.product_service.service;

import com.intuitveinc.common.model.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceTest {

    @Test
    void testCreateProduct() {
        IProductService IProductService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(IProductService.createProduct(product)).thenReturn(product);

        Product createdProduct = IProductService.createProduct(product);
        System.out.println(createdProduct);
        assertEquals("Refrigerator", createdProduct.getName());

        Mockito.when(IProductService.createProduct(product)).thenThrow(new RuntimeException("Product already exists"));
        assertThrows(RuntimeException.class, () -> IProductService.createProduct(product));

        Mockito.when(IProductService.createProduct(product)).thenReturn(null);
        assertNull(IProductService.createProduct(product));

        Mockito.when(IProductService.createProduct(null)).thenThrow(new RuntimeException("Product cannot be null"));
        assertThrows(RuntimeException.class, () -> IProductService.createProduct(null));
    }

    @Test
    void testGetProduct() {
        IProductService IProductService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(IProductService.getProduct(1L)).thenReturn(product);

        Product fetchedProduct = IProductService.getProduct(1L);
        System.out.println(fetchedProduct);
        assertEquals("Refrigerator", fetchedProduct.getName());

        Mockito.when(IProductService.getProduct(1L)).thenThrow(new RuntimeException("Product not found"));
        assertThrows(RuntimeException.class, () -> IProductService.getProduct(1L));

        Mockito.when(IProductService.getProduct(1L)).thenReturn(null);
        assertNull(IProductService.getProduct(1L));
    }

    @Test
    void testUpdateProduct() {
        IProductService IProductService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(IProductService.updateProduct(1L, product)).thenReturn(product);

        Product updatedProduct = IProductService.updateProduct(1L, product);
        System.out.println(updatedProduct);
        assertEquals("Refrigerator", updatedProduct.getName());

        Mockito.when(IProductService.updateProduct(1L, product)).thenThrow(new RuntimeException("Product not found"));
        assertThrows(RuntimeException.class, () -> IProductService.updateProduct(1L, product));

        Mockito.when(IProductService.updateProduct(1L, product)).thenReturn(null);
        assertNull(IProductService.updateProduct(1L, product));
    }

    @Test
    void testDeleteProduct() {
        IProductService IProductService = Mockito.mock(IProductService.class);
        Product product = new Product();
        product.setName("Refrigerator");
        Mockito.when(IProductService.deleteProduct(1L)).thenReturn(product);

        Product deletedProduct = IProductService.deleteProduct(1L);
        System.out.println(deletedProduct);
        assertEquals("Refrigerator", deletedProduct.getName());

        Mockito.when(IProductService.deleteProduct(1L)).thenThrow(new RuntimeException("Product not found"));
        assertThrows(RuntimeException.class, () -> IProductService.deleteProduct(1L));

        Mockito.when(IProductService.deleteProduct(1L)).thenReturn(null);
        assertNull(IProductService.deleteProduct(1L));
    }

    @Test
    void testGetAllProducts() {
        IProductService IProductService = Mockito.mock(IProductService.class);
        Product product1 = new Product();
        product1.setName("Refrigerator");
        Product product2 = new Product();
        product2.setName("Washing Machine");
        Mockito.when(IProductService.getAllProducts()).thenReturn(List.of(product1, product2));

        List<Product> products = IProductService.getAllProducts();
        System.out.println(products);
        assertEquals(2, products.size());

        Mockito.when(IProductService.getAllProducts()).thenReturn(Collections.emptyList());
        assertEquals(0, IProductService.getAllProducts().size());

        Mockito.when(IProductService.getAllProducts()).thenReturn(null);
        assertNull(IProductService.getAllProducts());

        Mockito.when(IProductService.getAllProducts()).thenThrow(new RuntimeException("No products found"));
        assertThrows(RuntimeException.class, () -> IProductService.getAllProducts());
    }
}
