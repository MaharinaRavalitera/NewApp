package com.daybyday.newapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate deadline;
    private String status;
    private Long clientId;
    private String clientName;
    private Double budget;
    private Double hoursSpent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
