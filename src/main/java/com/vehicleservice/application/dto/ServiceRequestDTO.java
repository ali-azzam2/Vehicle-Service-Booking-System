package com.vehicleservice.application.dto;

import com.vehicleservice.domain.entity.ServiceStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServiceRequestDTO {
    private Long id;
    private UserDTO user;
    private UserDTO mechanic;
    private String vehicleType;
    private String vehicleModel;
    private String vehicleNumber;
    private String serviceType;
    private String description;
    private ServiceStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
} 