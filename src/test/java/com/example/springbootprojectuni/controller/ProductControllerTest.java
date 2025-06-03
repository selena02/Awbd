package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.ProductRequest;
import com.example.springbootprojectuni.dto.ProductResponse;
import com.example.springbootprojectuni.mapper.ProductMapper;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.service.ProductService;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/products - addProduct() - Success")
    void addProduct_Success() throws Exception {


        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(99.99);
        productRequest.setCategory("Test Category");

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        product.setCategory("Test Category");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Test Product");
        savedProduct.setDescription("Test Description");
        savedProduct.setPrice(99.99);
        savedProduct.setCategory("Test Category");

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Test Product");
        productResponse.setDescription("Test Description");
        productResponse.setPrice(99.99);
        productResponse.setCategory("Test Category");

        when(productService.addProduct(any(ProductRequest.class))).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(productResponse);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.category").value("Test Category"))
                .andExpect(jsonPath("$.price").value(99.99));

        // Verify the service was called
        verify(productService, times(1)).addProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("GET /api/products - getAllProducts() - Success")
    void getAllProducts_Success() throws Exception {

        ProductResponse product1 = new ProductResponse();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setDescription("Desc1");
        product1.setPrice(10.0);
        product1.setCategory("Category1");

        ProductResponse product2 = new ProductResponse();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setDescription("Desc2");
        product2.setPrice(20.0);
        product2.setCategory("Category2");

        List<ProductResponse> productResponses = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(productResponses);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[0].category").value("Category1"))
                .andExpect(jsonPath("$[1].name").value("Product2"))
                .andExpect(jsonPath("$[1].category").value("Category2"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("GET /api/products/{id} - getProductById() - Success")
    void getProductById_Success() throws Exception {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("SingleProduct");
        productResponse.setDescription("SingleDesc");
        productResponse.setPrice(10.0);
        productResponse.setCategory("SingleCat");

        when(productService.getProductById(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("SingleProduct"))
                .andExpect(jsonPath("$.category").value("SingleCat"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - deleteProductById() - Success")
    void deleteProductById_Success() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProductById(1L);
    }

}
