package com.example.paymentservicedemo.repository;

import com.example.paymentservicedemo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
