package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.OrderResponse;
import com.example.springbootprojectuni.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints for orders interactions")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(
            summary = "Place a new order",
            description = "Converts the user's current cart into an order. The cart is cleared after placing the order.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder() {
        orderService.placeOrder();
        return ResponseEntity.ok("Order placed successfully.");
    }

    @Operation(
            summary = "Get all orders for the current user",
            description = "Retrieves a list of all orders placed by the currently authenticated user.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrdersForCurrentUser();
        return ResponseEntity.ok(orders);
    }
}
