package com.daybyday.newapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private String paymentNumber;
    private Long invoiceId;
    private String invoiceNumber;
    private Long clientId;
    private String clientName;
    private Double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
