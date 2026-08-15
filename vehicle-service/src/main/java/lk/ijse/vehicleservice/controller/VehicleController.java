package lk.ijse.vehicleservice.controller;

import lk.ijse.vehicleservice.dto.req.VehicleSaveRequest;
import lk.ijse.vehicleservice.dto.req.VehicleUpdateRequest;
import lk.ijse.vehicleservice.dto.resp.VehicleResponse;
import lk.ijse.vehicleservice.service.VehicleService;
import lk.ijse.vehicleservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> saveVehicle(
            @RequestBody VehicleSaveRequest request,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (!"DRIVER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Only drivers can register vehicles", null));
        }

        request.setOwnerId(userId);
        VehicleResponse response = vehicleService.saveVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Vehicle registered successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicle retrieved successfully", vehicleService.getVehicle(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getAllVehicles() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicles retrieved successfully", vehicleService.getAllVehicles()));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getVehiclesByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicles retrieved successfully", vehicleService.getVehiclesByOwner(ownerId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleUpdateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        VehicleResponse existing = vehicleService.getVehicle(id);
        if (!existing.getOwnerId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only update your own vehicles", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicle updated successfully", vehicleService.updateVehicle(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        VehicleResponse existing = vehicleService.getVehicle(id);
        if (!existing.getOwnerId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only delete your own vehicles", null));
        }

        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicle deleted successfully", null));
    }

    @PostMapping("/{id}/entry")
    public ResponseEntity<ApiResponse<VehicleResponse>> recordEntry(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicle entry recorded", vehicleService.recordEntry(id)));
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<ApiResponse<VehicleResponse>> recordExit(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Vehicle exit recorded", vehicleService.recordExit(id)));
    }
}