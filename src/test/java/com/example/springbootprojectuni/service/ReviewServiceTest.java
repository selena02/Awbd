package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.dto.ReviewRequest;
import com.example.springbootprojectuni.dto.ReviewResponse;
import com.example.springbootprojectuni.exception.ProductNotFoundException;
import com.example.springbootprojectuni.exception.UserNotFoundException;
import com.example.springbootprojectuni.mapper.ReviewMapper;
import com.example.springbootprojectuni.model.Product;
import com.example.springbootprojectuni.model.Review;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.repository.ProductRepository;
import com.example.springbootprojectuni.repository.ReviewRepository;
import com.example.springbootprojectuni.repository.UserRepository;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the SecurityContext and Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "mockToken");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("addReviewToProduct - Success")
    void addReviewToProduct_Success() {
        Long productId = 1L;
        Long userId = 2L;
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(5);
        reviewRequest.setReviewText("Fantastic product!");

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        Product product = new Product();
        product.setId(productId);
        product.setName("Sample Product");

        Review review = new Review();
        review.setRating(5);
        review.setReviewText("Fantastic product!");
        review.setUser(user);
        review.setProduct(product);

        Review savedReview = new Review();
        savedReview.setId(3L);
        savedReview.setRating(5);
        savedReview.setReviewText("Fantastic product!");
        savedReview.setUser(user);
        savedReview.setProduct(product);

        ReviewResponse response = new ReviewResponse();
        response.setId(3L);
        response.setRating(5);
        response.setReviewText("Fantastic product!");

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        when(reviewMapper.toResponse(savedReview)).thenReturn(response);

        ReviewResponse result = reviewService.addReviewToProduct(productId, reviewRequest);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Fantastic product!", result.getReviewText());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("getReviewsForProduct - Success")
    void getReviewsForProduct_Success() {
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(4);
        review1.setReviewText("Great!");

        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(5);
        review2.setReviewText("Amazing!");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProduct(product)).thenReturn(List.of(review1, review2));

        ReviewResponse response1 = new ReviewResponse();
        response1.setId(1L);
        response1.setRating(4);
        response1.setReviewText("Great!");

        ReviewResponse response2 = new ReviewResponse();
        response2.setId(2L);
        response2.setRating(5);
        response2.setReviewText("Amazing!");

        when(reviewMapper.toResponse(review1)).thenReturn(response1);
        when(reviewMapper.toResponse(review2)).thenReturn(response2);

        List<ReviewResponse> results = reviewService.getReviewsForProduct(productId);

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(4, results.get(0).getRating());

        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).findByProduct(product);
    }
}
