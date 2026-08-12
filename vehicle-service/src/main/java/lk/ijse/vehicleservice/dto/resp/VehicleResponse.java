package lk.ijse.vehicleservice.dto.resp;

import lk.ijse.vehicleservice.entity.VehicleStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
    private Long id;
    private String plateNumber;
    private String make;
    private String model;
    private String color;
    private Long ownerId;
    private VehicleStatus status;
    private LocalDateTime lastEntryTime;
    private LocalDateTime lastExitTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}