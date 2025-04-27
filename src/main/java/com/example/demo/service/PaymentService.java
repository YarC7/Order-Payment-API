package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    
    @Autowired
    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }
    
    @Transactional
    public Payment processPayment(PaymentRequest paymentRequest) {
        Order order = orderService.getOrderById(paymentRequest.getOrderId());
        
        if ("PAID".equals(order.getStatus())) {
            throw new IllegalStateException("Order has already been paid");
        }
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setAmount(order.getAmount());
        payment.setCurrency(order.getCurrency());
        payment.setTransactionId(generateTransactionId());
        
        if ("BANK".equalsIgnoreCase(paymentRequest.getPaymentMethod())) {
            payment.setBankName(paymentRequest.getBankName());
            payment.setAccountNumber(paymentRequest.getAccountNumber());
            payment.setStatus("PROCESSING");
        } else if ("CASH".equalsIgnoreCase(paymentRequest.getPaymentMethod())) {
            payment.setReceiptNumber(paymentRequest.getReceiptNumber());
            payment.setStatus("COMPLETED");
            orderService.updateOrderStatus(order.getId(), "PAID");
        } else {
            throw new IllegalArgumentException("Invalid payment method: " + paymentRequest.getPaymentMethod());
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }
    
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    
    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
    
    @Transactional
    public Payment updateBankPaymentStatus(Long paymentId, String status) {
        Payment payment = getPaymentById(paymentId);
        
        if (!"BANK".equalsIgnoreCase(payment.getPaymentMethod())) {
            throw new IllegalStateException("Can only update status for bank payments");
        }
        
        payment.setStatus(status);
        
        if ("COMPLETED".equals(status)) {
            orderService.updateOrderStatus(payment.getOrder().getId(), "PAID");
        }
        
        return paymentRepository.save(payment);
    }
}