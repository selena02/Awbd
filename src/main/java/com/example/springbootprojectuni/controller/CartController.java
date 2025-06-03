package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.CartResponse;
import com.example.springbootprojectuni.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Endpoints for cart interactions")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(
            summary = "Add an item to the cart",
            description = "Adds a specified quantity of a product to the user's cart.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addItemToCart(productId, quantity);
        return ResponseEntity.ok("Item added to cart successfully.");
    }

    @Operation(
            summary = "Get all items in the cart",
            description = "Retrieves the list of all items currently in the user's cart, along with the total price.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/items")
    public ResponseEntity<CartResponse> getCartItems() {
        CartResponse cartResponse = cartService.getAllItemsFromCart();
        return ResponseEntity.ok(cartResponse);
    }

    @Operation(
            summary = "Remove an item from the cart",
            description = "Removes a specified quantity of a product from the user's cart. If the quantity reaches zero, the product is removed from the cart.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItemFromCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.removeItemFromCart(productId, quantity);
        return ResponseEntity.ok("Item(s) removed from cart successfully.");
    }
}
