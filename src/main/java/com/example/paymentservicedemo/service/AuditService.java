package com.example.paymentservicedemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    public void recordPaymentAudit(Long orderId) {
        log.info("[AUDIT] Payment audit recorded for orderId={}", orderId);
        Random random = new Random();
        try {
            Thread.sleep(random.nextLong(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
