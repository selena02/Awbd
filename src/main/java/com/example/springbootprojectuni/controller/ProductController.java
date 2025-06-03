package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.ProductRequest;
import com.example.springbootprojectuni.dto.ProductResponse;
import com.example.springbootprojectuni.mapper.ProductMapper;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Endpoints for products interactions")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(
            summary = "Add a product",
            description = "Creates a new product in the system and returns the created product's details. Only Admins can perform this action",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product savedProduct = productService.addProduct(productRequest);
        ProductResponse response = productMapper.toResponse(savedProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all products with pagination and sorting",
            description = "Retrieves a paginated list of all available products, sorted as requested.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProductsPagedSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<ProductResponse> productPage = productService.getProductsPagedSorted(page, size, sortBy, direction);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Fetches the details of a product by its ID.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product by ID",
            description = "Deletes a product identified by its ID. Only Admins can perform this action.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Update product by ID",
            description = "Updates the details of an existing product identified by its ID. Only Admins can perform this action.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        Product updatedProduct = productService.updateProduct(id, productMapper.toEntity(productRequest));
        return new ResponseEntity<>(productMapper.toResponse(updatedProduct), HttpStatus.OK);
    }
}
