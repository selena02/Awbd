package com.example.springbootprojectuni.mapper;

import com.example.springbootprojectuni.dto.OrderItemResponse;
import com.example.springbootprojectuni.dto.OrderResponse;
import com.example.springbootprojectuni.model.Order;
import com.example.springbootprojectuni.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalPrice(order.getTotalPrice());
        response.setItems(
                order.getItems().stream().map(this::toItemResponse).collect(Collectors.toList())
        );
        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setQuantity(orderItem.getQuantity());
        return response;
    }
}
