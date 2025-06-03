package com.example.springbootprojectuni.mapper;

import com.example.springbootprojectuni.dto.ReviewRequest;
import com.example.springbootprojectuni.dto.ReviewResponse;
import com.example.springbootprojectuni.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setReviewText(reviewRequest.getReviewText());
        return review;
    }

    public ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setReviewText(review.getReviewText());
        response.setCreatedAt(review.getCreatedAt());
        response.setProductId(review.getProduct().getId());
        response.setUserId(review.getUser().getId());
        return response;
    }

}
