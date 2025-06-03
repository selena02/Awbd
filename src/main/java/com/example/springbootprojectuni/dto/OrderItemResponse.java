package com.example.springbootprojectuni.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
}
