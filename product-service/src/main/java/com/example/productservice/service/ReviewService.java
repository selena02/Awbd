package com.example.productservice.service;

import com.example.productservice.dto.ReviewRequest;
import com.example.productservice.dto.ReviewResponse;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.mapper.ReviewMapper;
import com.example.productservice.model.Product;
import com.example.productservice.model.Review;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse addReviewToProduct(Long productId, Long userId, ReviewRequest reviewRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        Review review = reviewMapper.toEntity(reviewRequest);
        review.setUserId(userId);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        List<Review> reviews = reviewRepository.findByProduct(product);

        return reviews.stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }
}
