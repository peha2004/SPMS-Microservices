package lk.ijse.vehicleservice.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleUpdateRequest {
    private String make;
    private String model;
    private String color;
}