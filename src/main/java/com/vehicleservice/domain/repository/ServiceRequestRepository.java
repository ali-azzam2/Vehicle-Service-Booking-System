package com.vehicleservice.domain.repository;

import com.vehicleservice.domain.entity.ServiceRequest;
import com.vehicleservice.domain.entity.ServiceStatus;
import com.vehicleservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByUser(User user);
    List<ServiceRequest> findByMechanic(User mechanic);
    List<ServiceRequest> findByStatus(ServiceStatus status);
    List<ServiceRequest> findByMechanicAndStatus(User mechanic, ServiceStatus status);
} 