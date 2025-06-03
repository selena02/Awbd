package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.dto.ReviewRequest;
import com.example.springbootprojectuni.dto.ReviewResponse;
import com.example.springbootprojectuni.exception.ProductNotFoundException;
import com.example.springbootprojectuni.exception.UserNotFoundException;
import com.example.springbootprojectuni.mapper.ReviewMapper;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.model.Review;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.repository.ProductRepository;
import com.example.springbootprojectuni.repository.ReviewRepository;
import com.example.springbootprojectuni.repository.UserRepository;
import com.example.springbootprojectuni.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse addReviewToProduct(Long productId, ReviewRequest reviewRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();


        Long userId = jwtUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setReviewText(reviewRequest.getReviewText());
        review.setUser(user);
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
