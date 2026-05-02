package com.example.paymentservicedemo.service;

import com.example.paymentservicedemo.entity.Order;
import com.example.paymentservicedemo.entity.OrderStatus;
import com.example.paymentservicedemo.entity.TransactionLog;
import com.example.paymentservicedemo.event.PaymentCompletedEvent;
import com.example.paymentservicedemo.repository.OrderRepository;
import com.example.paymentservicedemo.repository.TransactionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final OrderRepository orderRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AuditService auditService;

    public PaymentService(OrderRepository orderRepository,
                          TransactionLogRepository transactionLogRepository,
                          ApplicationEventPublisher eventPublisher,
                          AuditService auditService) {
        this.orderRepository = orderRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
    }

    @Transactional
    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        transactionLogRepository.save(
                new TransactionLog(orderId, "PAYMENT_COMPLETED", LocalDateTime.now())
        );
        eventPublisher.publishEvent(new PaymentCompletedEvent(orderId));
        auditService.recordPaymentAudit(orderId);
    }
}
