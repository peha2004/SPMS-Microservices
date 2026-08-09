package lk.ijse.parkingservice.repository;

import lk.ijse.parkingservice.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByCityContainingIgnoreCaseOrZoneContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String city, String zone, String location
    );
}