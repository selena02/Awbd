package com.example.productservice.mapper;

import com.example.productservice.dto.ReviewRequest;
import com.example.productservice.dto.ReviewResponse;
import com.example.productservice.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setReviewText(reviewRequest.getReviewText());
        // userId will be set in the service
        return review;
    }

    public ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setReviewText(review.getReviewText());
        response.setCreatedAt(review.getCreatedAt());
        response.setProductId(review.getProduct().getId());
        response.setUserId(review.getUserId());
        return response;
    }

}
