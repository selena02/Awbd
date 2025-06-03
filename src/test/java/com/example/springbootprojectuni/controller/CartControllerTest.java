package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.CartResponse;
import com.example.springbootprojectuni.service.CartService;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("POST /api/cart/add - addItemToCart - Success")
    void addItemToCart_Success() throws Exception {
        doNothing().when(cartService).addItemToCart(anyLong(), anyInt());

        mockMvc.perform(post("/api/cart/add")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added to cart successfully."));
    }

    @Test
    @DisplayName("GET /api/cart/items - getCartItems - Success")
    void getCartItems_Success() throws Exception {
        CartResponse response = new CartResponse();
        when(cartService.getAllItemsFromCart()).thenReturn(response);

        mockMvc.perform(get("/api/cart/items"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/cart/remove - removeItemFromCart - Success")
    void removeItemFromCart_Success() throws Exception {
        doNothing().when(cartService).removeItemFromCart(anyLong(), anyInt());

        mockMvc.perform(delete("/api/cart/remove")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item(s) removed from cart successfully."));
    }
}
