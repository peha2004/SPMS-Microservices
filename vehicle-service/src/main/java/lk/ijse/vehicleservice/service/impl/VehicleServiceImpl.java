package lk.ijse.vehicleservice.service.impl;

import lk.ijse.vehicleservice.dto.req.VehicleSaveRequest;
import lk.ijse.vehicleservice.dto.req.VehicleUpdateRequest;
import lk.ijse.vehicleservice.dto.resp.VehicleResponse;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.entity.VehicleStatus;
import lk.ijse.vehicleservice.exception.BadRequestException;
import lk.ijse.vehicleservice.exception.DuplicatePlateException;
import lk.ijse.vehicleservice.exception.VehicleNotFoundException;
import lk.ijse.vehicleservice.repository.VehicleRepository;
import lk.ijse.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponse saveVehicle(VehicleSaveRequest request) {
        if (request.getPlateNumber() == null || request.getPlateNumber().trim().isEmpty())
            throw new BadRequestException("Plate number is required");
        if (request.getMake() == null || request.getMake().trim().isEmpty())
            throw new BadRequestException("Make is required");
        if (request.getModel() == null || request.getModel().trim().isEmpty())
            throw new BadRequestException("Model is required");
        if (request.getOwnerId() == null || request.getOwnerId() <= 0)
            throw new BadRequestException("Valid owner ID is required");
        if (vehicleRepository.existsByPlateNumber(request.getPlateNumber()))
            throw new DuplicatePlateException("Vehicle with this plate number already exists");

        Vehicle vehicle = Vehicle.builder()
                .plateNumber(request.getPlateNumber())
                .make(request.getMake())
                .model(request.getModel())
                .color(request.getColor())
                .ownerId(request.getOwnerId())
                .status(VehicleStatus.OUT)
                .build();

        return mapToResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponse updateVehicle(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = findVehicle(id);
        if (request.getMake() != null) vehicle.setMake(request.getMake());
        if (request.getModel() != null) vehicle.setModel(request.getModel());
        if (request.getColor() != null) vehicle.setColor(request.getColor());
        return mapToResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponse getVehicle(Long id) {
        return mapToResponse(findVehicle(id));
    }

    @Override
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<VehicleResponse> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.delete(findVehicle(id));
    }

    @Override
    public VehicleResponse recordEntry(Long id) {
        Vehicle vehicle = findVehicle(id);
        if (vehicle.getStatus() == VehicleStatus.IN) {
            throw new BadRequestException("Vehicle is already marked as IN");
        }
        vehicle.setStatus(VehicleStatus.IN);
        vehicle.setLastEntryTime(LocalDateTime.now());
        return mapToResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponse recordExit(Long id) {
        Vehicle vehicle = findVehicle(id);
        if (vehicle.getStatus() == VehicleStatus.OUT) {
            throw new BadRequestException("Vehicle is already marked as OUT");
        }
        vehicle.setStatus(VehicleStatus.OUT);
        vehicle.setLastExitTime(LocalDateTime.now());
        return mapToResponse(vehicleRepository.save(vehicle));
    }

    private Vehicle findVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .ownerId(vehicle.getOwnerId())
                .status(vehicle.getStatus())
                .lastEntryTime(vehicle.getLastEntryTime())
                .lastExitTime(vehicle.getLastExitTime())
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }
}