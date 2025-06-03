package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.dto.OrderResponse;
import com.example.springbootprojectuni.exception.CartNotFoundException;
import com.example.springbootprojectuni.exception.UserNotFoundException;
import com.example.springbootprojectuni.mapper.OrderMapper;
import com.example.springbootprojectuni.model.*;
import com.example.springbootprojectuni.repository.CartRepository;
import com.example.springbootprojectuni.repository.OrderRepository;
import com.example.springbootprojectuni.repository.UserRepository;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("placeOrder - Success")
    void placeOrder_Success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(50.0);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.placeOrder();

        verify(cartRepository, times(1)).save(cart);
        verify(orderRepository, times(1)).save(any(Order.class));
        assertTrue(cart.getItems().isEmpty()); // Ensure items are cleared
    }

    @Test
    @DisplayName("placeOrder - Empty Cart")
    void placeOrder_EmptyCart() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(List.of());

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder());
    }

    @Test
    @DisplayName("getAllOrdersForCurrentUser - Success")
    void getAllOrdersForCurrentUser_Success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        OrderResponse response1 = new OrderResponse();
        response1.setId(1L);

        OrderResponse response2 = new OrderResponse();
        response2.setId(2L);

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(List.of(order1, order2));
        when(orderMapper.toResponse(order1)).thenReturn(response1);
        when(orderMapper.toResponse(order2)).thenReturn(response2);

        List<OrderResponse> results = orderService.getAllOrdersForCurrentUser();

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        verify(orderRepository, times(1)).findByUser(user);
    }
}
