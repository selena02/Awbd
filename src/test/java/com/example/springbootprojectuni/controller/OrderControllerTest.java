package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.OrderResponse;
import com.example.springbootprojectuni.service.OrderService;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("POST /api/orders/place - placeOrder - Success")
    void placeOrder_Success() throws Exception {
        doNothing().when(orderService).placeOrder();

        mockMvc.perform(post("/api/orders/place"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order placed successfully."));

        verify(orderService, times(1)).placeOrder();
    }

    @Test
    @DisplayName("GET /api/orders - getAllOrders - Success")
    void getAllOrders_Success() throws Exception {
        OrderResponse order1 = new OrderResponse();
        order1.setId(1L);
        order1.setTotalPrice(BigDecimal.valueOf(10000));

        OrderResponse order2 = new OrderResponse();
        order2.setId(2L);
        order2.setTotalPrice(BigDecimal.valueOf(200.00));

        List<OrderResponse> orders = List.of(order1, order2);
        when(orderService.getAllOrdersForCurrentUser()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalPrice").value(10000))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].totalPrice").value(200.00));

        verify(orderService, times(1)).getAllOrdersForCurrentUser();
    }
}
