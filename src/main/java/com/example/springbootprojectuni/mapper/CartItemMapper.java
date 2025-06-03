package com.example.springbootprojectuni.mapper;

import com.example.springbootprojectuni.dto.CartItemResponse;
import com.example.springbootprojectuni.model.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper {

    public CartItemResponse toResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(
                BigDecimal.valueOf(cartItem.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );
        return response;
    }
}
