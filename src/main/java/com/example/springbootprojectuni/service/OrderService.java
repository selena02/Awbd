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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void placeOrder() {
        // Retrieve user details from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();

        Long userId = jwtUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user ID: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot place an order.");
        }


        BigDecimal totalPrice = cart.getItems().stream()
                .map(cartItem -> BigDecimal.valueOf(cartItem.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);

        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getItems().add(orderItem);
        });


        orderRepository.save(order);

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersForCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();

        Long userId = jwtUtil.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }

}
