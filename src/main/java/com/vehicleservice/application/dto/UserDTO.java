package com.vehicleservice.application.dto;

import com.vehicleservice.domain.entity.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private UserRole role;
} 