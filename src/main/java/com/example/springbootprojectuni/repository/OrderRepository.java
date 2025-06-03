package com.example.springbootprojectuni.repository;

import com.example.springbootprojectuni.model.Order;
import com.example.springbootprojectuni.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
