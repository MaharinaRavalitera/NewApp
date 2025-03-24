package com.daybyday.newapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private Long id;
    private String invoiceNumber;
    private Long clientId;
    private String clientName;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private Double amount;
    private Double amountPaid;
    private String status;
    private String description;
    private Long projectId;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
