package com.example.productservice.repository;

import com.example.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.productservice.model.Review;

import java.util.List;

public interface ReviewRepository  extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}
