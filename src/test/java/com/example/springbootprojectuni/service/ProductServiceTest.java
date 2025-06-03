package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.dto.ProductRequest;
import com.example.springbootprojectuni.dto.ProductResponse;
import com.example.springbootprojectuni.exception.ProductNotFoundException;
import com.example.springbootprojectuni.mapper.ProductMapper;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("addProduct - Success")
    void addProduct_Success() {
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setDescription("A product description");
        request.setPrice(100.0);
        request.setCategory("TestCategory");

        Product productToSave = new Product();
        productToSave.setName("Test Product");
        productToSave.setDescription("A product description");
        productToSave.setPrice(100.0);
        productToSave.setCategory("TestCategory");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Test Product");
        savedProduct.setDescription("A product description");
        savedProduct.setPrice(100.0);
        savedProduct.setCategory("TestCategory");

        when(productMapper.toEntity(request)).thenReturn(productToSave);
        when(productRepository.save(productToSave)).thenReturn(savedProduct);

        Product result = productService.addProduct(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals("TestCategory", result.getCategory());

        verify(productMapper, times(1)).toEntity(request);
        verify(productRepository, times(1)).save(productToSave);
    }

    @Test
    @DisplayName("getAllProducts - Returns List<ProductResponse>")
    void getAllProducts_Success() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("P1");
        product1.setDescription("Desc1");
        product1.setPrice(10.0);
        product1.setCategory("Cat1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("P2");
        product2.setDescription("Desc2");
        product2.setPrice(20.0);
        product2.setCategory("Cat2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        ProductResponse response1 = new ProductResponse();
        response1.setId(1L);
        response1.setName("P1");
        response1.setDescription("Desc1");
        response1.setPrice(10.0);
        response1.setCategory("Cat1");

        ProductResponse response2 = new ProductResponse();
        response2.setId(2L);
        response2.setName("P2");
        response2.setDescription("Desc2");
        response2.setPrice(20.0);
        response2.setCategory("Cat2");

        when(productMapper.toResponse(product1)).thenReturn(response1);
        when(productMapper.toResponse(product2)).thenReturn(response2);

        List<ProductResponse> results = productService.getAllProducts();

        assertEquals(2, results.size());
        assertEquals("P1", results.get(0).getName());
        assertEquals("P2", results.get(1).getName());

        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toResponse(product1);
        verify(productMapper, times(1)).toResponse(product2);
    }

    @Test
    @DisplayName("getProductById - Success")
    void getProductById_Success() {
        Product product = new Product();
        product.setId(10L);
        product.setName("MyProduct");
        product.setDescription("Desc");
        product.setPrice(50.0);
        product.setCategory("CatX");

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        ProductResponse response = new ProductResponse();
        response.setId(10L);
        response.setName("MyProduct");
        response.setDescription("Desc");
        response.setPrice(50.0);
        response.setCategory("CatX");

        when(productMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.getProductById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("MyProduct", result.getName());
        verify(productRepository, times(1)).findById(10L);
        verify(productMapper, times(1)).toResponse(product);
    }

    @Test
    @DisplayName("deleteProductById - Success")
    void deleteProductById_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProductById(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("updateProduct - Success")
    void updateProduct_Success() {
        Product existingProduct = new Product();
        existingProduct.setId(5L);
        existingProduct.setName("OldName");
        existingProduct.setDescription("OldDesc");
        existingProduct.setPrice(10.0);
        existingProduct.setCategory("OldCat");

        when(productRepository.findById(5L)).thenReturn(Optional.of(existingProduct));

        Product updatedInput = new Product();
        updatedInput.setName("NewName");
        updatedInput.setDescription("NewDesc");
        updatedInput.setPrice(99.99);
        updatedInput.setCategory("NewCat");

        Product savedProduct = new Product();
        savedProduct.setId(5L);
        savedProduct.setName("NewName");
        savedProduct.setDescription("NewDesc");
        savedProduct.setPrice(99.99);
        savedProduct.setCategory("OldCat");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.updateProduct(5L, updatedInput);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("NewName", result.getName());
        assertEquals("NewDesc", result.getDescription());
        assertEquals(99.99, result.getPrice());

        verify(productRepository, times(1)).findById(5L);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
