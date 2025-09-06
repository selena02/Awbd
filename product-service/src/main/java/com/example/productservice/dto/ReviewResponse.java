package com.example.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;
    private Long productId;
    private Long userId;
}
