package lk.ijse.parkingservice.service;

import lk.ijse.parkingservice.dto.req.ParkingSaveRequest;
import lk.ijse.parkingservice.dto.req.ParkingUpdateRequest;
import lk.ijse.parkingservice.dto.resp.ParkingResponse;

import java.util.List;

public interface ParkingService {
    ParkingResponse saveParking(ParkingSaveRequest request);
    ParkingResponse updateParking(Long id, ParkingUpdateRequest request);
    ParkingResponse getParking(Long id);
    List<ParkingResponse> getAllParking();
    void deleteParking(Long id);
    List<ParkingResponse> filterByLocation(String location);
    ParkingResponse reserveParking(Long id);
    ParkingResponse releaseParking(Long id);
}