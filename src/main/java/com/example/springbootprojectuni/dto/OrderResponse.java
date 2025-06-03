package com.example.springbootprojectuni.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private List<OrderItemResponse> items;
}
