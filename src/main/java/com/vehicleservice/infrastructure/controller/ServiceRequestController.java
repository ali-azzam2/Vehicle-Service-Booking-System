package com.vehicleservice.infrastructure.controller;

import com.vehicleservice.application.dto.ServiceRequestDTO;
import com.vehicleservice.application.dto.request.CreateServiceRequest;
import com.vehicleservice.application.service.ServiceRequestService;
import com.vehicleservice.domain.entity.ServiceStatus;
import com.vehicleservice.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-requests")
@RequiredArgsConstructor
public class ServiceRequestController {
    private final ServiceRequestService serviceRequestService;
    private final SecurityUtils securityUtils;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ServiceRequestDTO> createServiceRequest(
            @Valid @RequestBody CreateServiceRequest request) {
        ServiceRequestDTO serviceRequest = serviceRequestService.createServiceRequest(
                securityUtils.getCurrentUser().getId(), request);
        return ResponseEntity.ok(serviceRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestDTO> getServiceRequest(@PathVariable Long id) {
        ServiceRequestDTO serviceRequest = serviceRequestService.getServiceRequestById(id);
        return ResponseEntity.ok(serviceRequest);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ServiceRequestDTO>> getUserServiceRequests() {
        List<ServiceRequestDTO> serviceRequests = serviceRequestService.getServiceRequestsByUser(
                securityUtils.getCurrentUser().getId());
        return ResponseEntity.ok(serviceRequests);
    }

    @GetMapping("/mechanic")
    @PreAuthorize("hasRole('MECHANIC')")
    public ResponseEntity<List<ServiceRequestDTO>> getMechanicServiceRequests() {
        List<ServiceRequestDTO> serviceRequests = serviceRequestService.getAll();
        return ResponseEntity.ok(serviceRequests);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('MECHANIC')")
    public ResponseEntity<List<ServiceRequestDTO>> getServiceRequestsByStatus(
            @PathVariable ServiceStatus status) {
        List<ServiceRequestDTO> serviceRequests = serviceRequestService.getServiceRequestsByStatus(status);
        return ResponseEntity.ok(serviceRequests);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('MECHANIC')")
    public ResponseEntity<ServiceRequestDTO> updateServiceStatus(
            @PathVariable Long id,
            @RequestParam ServiceStatus status) {
        ServiceRequestDTO serviceRequest = serviceRequestService.updateServiceStatus(
                id, status, securityUtils.getCurrentUser().getId());
        return ResponseEntity.ok(serviceRequest);
    }
} 