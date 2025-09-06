package com.example.productservice.controller;

import com.example.productservice.dto.ReviewRequest;
import com.example.productservice.dto.ReviewResponse;
import com.example.productservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@Tag(name = "Reviews", description = "Endpoints for product reviews interactions")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(
            summary = "Add a review to a product",
            description = "Adds a review for the product identified by its ID.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@PathVariable Long productId,
                                                    @RequestParam Long userId,
                                                    @Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse createdReview = reviewService.addReviewToProduct(productId, userId, reviewRequest);
        return ResponseEntity.ok(createdReview);
    }

    @Operation(
            summary = "Get all reviews for a product",
            description = "Retrieves a list of reviews for the product identified by its ID.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsForProduct(@PathVariable Long productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }
}
