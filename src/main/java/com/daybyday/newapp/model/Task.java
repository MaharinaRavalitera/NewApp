package com.daybyday.newapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDate deadline;
    private Long projectId;
    private String projectName;
    private Long assignedUserId;
    private String assignedUserName;
    private Integer hoursAllocated;
    private Integer hoursSpent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
