package com.example.paymentservicedemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_logs")
@Getter
@NoArgsConstructor
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public TransactionLog(Long orderId, String eventType, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.createdAt = createdAt;
    }
}
