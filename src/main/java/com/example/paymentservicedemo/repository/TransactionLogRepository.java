package com.example.paymentservicedemo.repository;

import com.example.paymentservicedemo.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
