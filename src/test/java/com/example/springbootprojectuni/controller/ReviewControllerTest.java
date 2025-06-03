package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.ReviewRequest;
import com.example.springbootprojectuni.dto.ReviewResponse;
import com.example.springbootprojectuni.service.ReviewService;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/products/{productId}/reviews - addReview - Success")
    void addReview_Success() throws Exception {
        Long productId = 1L;

        // Mock request
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(5);
        reviewRequest.setReviewText("Great product!");

        // Mock response
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(1L);
        reviewResponse.setRating(5);
        reviewResponse.setReviewText("Great product!");

        when(reviewService.addReviewToProduct(eq(productId), any(ReviewRequest.class))).thenReturn(reviewResponse);

        mockMvc.perform(post("/api/products/{productId}/reviews", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Great product!"));

        verify(reviewService, times(1)).addReviewToProduct(eq(productId), any(ReviewRequest.class));
    }

    @Test
    @DisplayName("GET /api/products/{productId}/reviews - getReviewsForProduct - Success")
    void getReviewsForProduct_Success() throws Exception {
        Long productId = 1L;

        // Mock response
        ReviewResponse review1 = new ReviewResponse();
        review1.setId(1L);
        review1.setRating(5);
        review1.setReviewText("Amazing!");

        ReviewResponse review2 = new ReviewResponse();
        review2.setId(2L);
        review2.setRating(4);
        review2.setReviewText("Very good!");

        List<ReviewResponse> reviews = List.of(review1, review2);

        when(reviewService.getReviewsForProduct(eq(productId))).thenReturn(reviews);

        mockMvc.perform(get("/api/products/{productId}/reviews", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].reviewText").value("Amazing!"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].rating").value(4))
                .andExpect(jsonPath("$[1].reviewText").value("Very good!"));

        verify(reviewService, times(1)).getReviewsForProduct(eq(productId));
    }
}
