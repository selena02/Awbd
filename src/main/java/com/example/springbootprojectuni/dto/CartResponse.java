package com.example.springbootprojectuni.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
}
