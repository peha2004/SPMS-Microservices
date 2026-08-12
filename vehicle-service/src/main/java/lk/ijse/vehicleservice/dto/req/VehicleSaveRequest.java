package lk.ijse.vehicleservice.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleSaveRequest {
    private String plateNumber;
    private String make;
    private String model;
    private String color;
    private Long ownerId;
}