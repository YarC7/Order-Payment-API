package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String paymentMethod; // BANK or CASH
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime paymentDate;
    private String status;

    // For bank payments
    private String bankName;
    private String accountNumber;

    // For cash payments
    private String receiptNumber;

    @PrePersist
    public void prePersist() {
        this.paymentDate = LocalDateTime.now();
    }
}