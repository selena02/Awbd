package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.dto.ProductRequest;
import com.example.springbootprojectuni.dto.ProductResponse;
import com.example.springbootprojectuni.exception.ProductNotFoundException;
import com.example.springbootprojectuni.mapper.ProductMapper;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public Product addProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        return productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        return productMapper.toResponse(product);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }
}
