package com.daybyday.newapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private Long id;
    private String name;
    private String email;
    private String primaryNumber;
    private String secondaryNumber;
    private String address;
    private String zipCode;
    private String city;
    private String country;
    private String industry;
    private String companyName;
    private String vatNumber;
    private String contactPerson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
