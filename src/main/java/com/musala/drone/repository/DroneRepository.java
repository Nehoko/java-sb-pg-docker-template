package com.musala.drone.repository;

import com.musala.drone.entity.Drone;
import com.musala.drone.enums.drone.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    Optional<Drone> findDroneBySerialNumber(String serialNumber);

    List<Drone> findAllByStateIsInAndBatteryCapacityGreaterThanEqual(List<DroneState> state, Integer batteryCapacity);
}
