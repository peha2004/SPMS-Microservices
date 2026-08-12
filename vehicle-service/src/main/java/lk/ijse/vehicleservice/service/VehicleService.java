package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.req.VehicleSaveRequest;
import lk.ijse.vehicleservice.dto.req.VehicleUpdateRequest;
import lk.ijse.vehicleservice.dto.resp.VehicleResponse;

import java.util.List;

public interface VehicleService {
    VehicleResponse saveVehicle(VehicleSaveRequest request);
    VehicleResponse updateVehicle(Long id, VehicleUpdateRequest request);
    VehicleResponse getVehicle(Long id);
    List<VehicleResponse> getAllVehicles();
    List<VehicleResponse> getVehiclesByOwner(Long ownerId);
    void deleteVehicle(Long id);
    VehicleResponse recordEntry(Long id);
    VehicleResponse recordExit(Long id);
}