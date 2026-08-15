package lk.ijse.parkingservice.controller;

import lk.ijse.parkingservice.dto.req.ParkingSaveRequest;
import lk.ijse.parkingservice.dto.req.ParkingUpdateRequest;
import lk.ijse.parkingservice.dto.resp.ParkingResponse;
import lk.ijse.parkingservice.service.ParkingService;
import lk.ijse.parkingservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@CrossOrigin
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingResponse>> saveParking(
            @RequestBody ParkingSaveRequest request,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (!"OWNER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Only parking owners can create parking spaces", null));
        }

        request.setOwnerId(userId);
        ParkingResponse response = parkingService.saveParking(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Parking created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingResponse>> getParking(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Parking retrieved successfully", parkingService.getParking(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParkingResponse>>> getAllParking() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Parking list retrieved successfully", parkingService.getAllParking()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingResponse>> updateParking(
            @PathVariable Long id,
            @RequestBody ParkingUpdateRequest request,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (!"OWNER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Only parking owners can update parking spaces", null));
        }

        ParkingResponse existing = parkingService.getParking(id);
        if (!existing.getOwnerId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only update your own parking spaces", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "Parking updated successfully", parkingService.updateParking(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParking(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (!"OWNER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Only parking owners can delete parking spaces", null));
        }

        ParkingResponse existing = parkingService.getParking(id);
        if (!existing.getOwnerId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only delete your own parking spaces", null));
        }

        parkingService.deleteParking(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Parking deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ParkingResponse>>> filterByLocation(@RequestParam String location) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Parking filtered successfully", parkingService.filterByLocation(location)));
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ApiResponse<ParkingResponse>> reserveParking(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Parking reserved successfully", parkingService.reserveParking(id)));
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<ApiResponse<ParkingResponse>> releaseParking(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Parking released successfully", parkingService.releaseParking(id)));
    }
}