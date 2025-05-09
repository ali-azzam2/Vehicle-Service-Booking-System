package com.vehicleservice.application.service;

import com.vehicleservice.application.dto.ServiceRequestDTO;
import com.vehicleservice.application.dto.request.CreateServiceRequest;
import com.vehicleservice.domain.entity.ServiceRequest;
import com.vehicleservice.domain.entity.ServiceStatus;
import com.vehicleservice.domain.entity.User;
import com.vehicleservice.domain.repository.ServiceRequestRepository;
import com.vehicleservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public ServiceRequestDTO createServiceRequest(Long userId, CreateServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setUser(user);
        serviceRequest.setVehicleType(request.getVehicleType());
        serviceRequest.setVehicleModel(request.getVehicleModel());
        serviceRequest.setVehicleNumber(request.getVehicleNumber());
        serviceRequest.setServiceType(request.getServiceType());
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setStatus(ServiceStatus.PENDING);
        serviceRequest.setRequestedAt(LocalDateTime.now());

        ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);
        return convertToDTO(savedRequest);
    }

    @Transactional(readOnly = true)
    public ServiceRequestDTO getServiceRequestById(Long id) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));
        return convertToDTO(serviceRequest);
    }

    @Transactional(readOnly = true)
    public List<ServiceRequestDTO> getServiceRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return serviceRequestRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServiceRequestDTO> getServiceRequestsByMechanic(Long mechanicId) {
        User mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        return serviceRequestRepository.findByMechanic(mechanic).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServiceRequestDTO> getServiceRequestsByStatus(ServiceStatus status) {
        return serviceRequestRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceRequestDTO updateServiceStatus(Long id, ServiceStatus status, Long mechanicId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found"));

        User mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        serviceRequest.setMechanic(mechanic);
        serviceRequest.setStatus(status);

        if (status == ServiceStatus.ACCEPTED) {
            serviceRequest.setAcceptedAt(LocalDateTime.now());
        } else if (status == ServiceStatus.COMPLETED) {
            serviceRequest.setCompletedAt(LocalDateTime.now());
        }

        ServiceRequest updatedRequest = serviceRequestRepository.save(serviceRequest);
        return convertToDTO(updatedRequest);
    }

    private ServiceRequestDTO convertToDTO(ServiceRequest serviceRequest) {
        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(serviceRequest.getId());
        dto.setUser(userService.getUserById(serviceRequest.getUser().getId()));
        if (serviceRequest.getMechanic() != null) {
            dto.setMechanic(userService.getUserById(serviceRequest.getMechanic().getId()));
        }
        dto.setVehicleType(serviceRequest.getVehicleType());
        dto.setVehicleModel(serviceRequest.getVehicleModel());
        dto.setVehicleNumber(serviceRequest.getVehicleNumber());
        dto.setServiceType(serviceRequest.getServiceType());
        dto.setDescription(serviceRequest.getDescription());
        dto.setStatus(serviceRequest.getStatus());
        dto.setRequestedAt(serviceRequest.getRequestedAt());
        dto.setAcceptedAt(serviceRequest.getAcceptedAt());
        dto.setCompletedAt(serviceRequest.getCompletedAt());
        return dto;
    }

    public List<ServiceRequestDTO> getAll() {
        return serviceRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
} 