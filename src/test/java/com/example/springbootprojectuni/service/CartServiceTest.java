package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.dto.CartItemResponse;
import com.example.springbootprojectuni.dto.CartResponse;
import com.example.springbootprojectuni.exception.CartNotFoundException;
import com.example.springbootprojectuni.exception.ProductNotFoundException;
import com.example.springbootprojectuni.exception.UserNotFoundException;
import com.example.springbootprojectuni.mapper.CartItemMapper;
import com.example.springbootprojectuni.model.Cart;
import com.example.springbootprojectuni.model.CartItem;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.repository.CartRepository;
import com.example.springbootprojectuni.repository.ProductRepository;
import com.example.springbootprojectuni.repository.UserRepository;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartService cartService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContextHolder
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock authentication credentials (JWT token)
        when(authentication.getCredentials()).thenReturn("mock-token");
    }

    @Test
    @DisplayName("addItemToCart - Success")
    void addItemToCart_Success() {
        Long userId = 1L;
        Long productId = 2L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(10.0);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(1);
        cart.setItems(List.of(existingItem));

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.addItemToCart(productId, 2);

        verify(cartRepository, times(1)).save(cart);
        assertEquals(3, existingItem.getQuantity());
    }

    @Test
    @DisplayName("getAllItemsFromCart - Success")
    void getAllItemsFromCart_Success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(2L);
        product.setPrice(10.0);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.setItems(List.of(cartItem));

        CartItemResponse itemResponse = new CartItemResponse();
        itemResponse.setProductId(2L);
        itemResponse.setQuantity(2);

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemMapper.toResponse(cartItem)).thenReturn(itemResponse);

        CartResponse response = cartService.getAllItemsFromCart();

        assertNotNull(response);
        assertEquals(2, response.getItems().get(0).getQuantity());
        verify(cartRepository, times(1)).findByUser(user);
    }

    @Test
    @DisplayName("removeItemFromCart - Success")
    void removeItemFromCart_Success() {
        Long userId = 1L;
        Long productId = 2L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(3);
        cart.setItems(List.of(cartItem));

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.removeItemFromCart(productId, 2);

        verify(cartRepository, times(1)).save(cart);
        assertEquals(1, cartItem.getQuantity());
    }
}
