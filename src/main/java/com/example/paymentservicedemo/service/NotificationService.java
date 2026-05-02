package com.example.paymentservicedemo.service;

import com.example.paymentservicedemo.entity.Order;
import com.example.paymentservicedemo.entity.TransactionLog;
import com.example.paymentservicedemo.event.PaymentCompletedEvent;
import com.example.paymentservicedemo.repository.OrderRepository;
import com.example.paymentservicedemo.repository.TransactionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final OrderRepository orderRepository;
    private final TransactionLogRepository transactionLogRepository;

    public NotificationService(OrderRepository orderRepository,
                                TransactionLogRepository transactionLogRepository) {
        this.orderRepository = orderRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        Long orderId = event.orderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.setNotificationSent(true);
        orderRepository.save(order);
        transactionLogRepository.save(
                new TransactionLog(orderId, "NOTIFICATION_SENT", LocalDateTime.now())
        );
    }
}
