package com.daybyday.newapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lead {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String source;
    private Double value;
    private Long clientId;
    private String clientName;
    private Long assignedUserId;
    private String assignedUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
