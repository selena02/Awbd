package com.example.springbootprojectuni.repository;

import com.example.springbootprojectuni.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springbootprojectuni.model.Review;

import java.util.List;

public interface ReviewRepository  extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}
