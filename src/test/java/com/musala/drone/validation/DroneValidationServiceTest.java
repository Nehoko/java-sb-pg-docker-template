package com.musala.drone.validation;

import com.musala.drone.entity.Drone;
import com.musala.drone.entity.Medication;
import com.musala.drone.enums.DroneModel;
import com.musala.drone.enums.DroneState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DroneValidationServiceTest {

    @Autowired
    private DroneValidationService droneValidationService;

    @Test
    void validateWeight_returnError() {
        List<Medication> medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                500,
                "PCTML-500", "http://example.com"));
        Drone drone = new Drone(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                medicationList);

        Optional<String> errorMessage = droneValidationService.validate(drone);
        assertTrue(errorMessage.isPresent());
        assertEquals(String.format(DroneValidationService.OVERLOAD_ERROR, drone.getSerialNumber()), errorMessage.get());
    }

    @Test
    void validateState_returnError() {
        List<Medication> medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                99,
                "PCTML-500", "http://example.com"));
        Drone drone = new Drone(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                24,
                DroneState.LOADING,
                medicationList);

        Optional<String> errorMessage = droneValidationService.validate(drone);
        assertTrue(errorMessage.isPresent());
        assertEquals(String.format(DroneValidationService.LOW_BATTERY_ERROR, drone.getSerialNumber()), errorMessage.get());
    }

    @Test
    void validateSerialNumber_returnError() {

        // serialNumber is null
        List<Medication> medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                99,
                "PCTML-500", "http://example.com"));
        Drone drone = new Drone(
                1L,
                null,
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.LOADING,
                medicationList);
        Optional<String> errorMessage = droneValidationService.validate(drone);
        assertTrue(errorMessage.isPresent());
        assertEquals(String.format(DroneValidationService.INVALID_SERIAL_NUMBER_ERROR, drone.getSerialNumber()), errorMessage.get());

        // serial number length is more than 100
        medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                99,
                "PCTML-500", "http://example.com"));
        drone = new Drone(
                1L,
                "a".repeat(101),
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.LOADING,
                medicationList);
        errorMessage = droneValidationService.validate(drone);
        assertTrue(errorMessage.isPresent());
        assertEquals(String.format(DroneValidationService.INVALID_SERIAL_NUMBER_ERROR, drone.getSerialNumber()), errorMessage.get());

    }

    @Test
    void validateWeightLimit_returnError() {

        // weight limit is more than 500g
        List<Medication> medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                99,
                "PCTML-500", "http://example.com"));
        Drone drone = new Drone(
                1L,
                "zephyr-15",
                DroneModel.HEAVYWEIGHT,
                501,
                99,
                DroneState.LOADING,
                medicationList);
        Optional<String> errorMessage = droneValidationService.validate(drone);
        assertTrue(errorMessage.isPresent());
        assertEquals(String.format(DroneValidationService.WEIGHT_LIMIT_ERROR, drone.getSerialNumber()), errorMessage.get());
    }

    @Test
    void validateBatteryCapacity_returnError() {

        // battery capacity is more than 100%
        List<Medication> medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                99,
                "PCTML-500", "http://example.com"));
        Drone drone = new Drone(
                1L,
                "zephyr-15",
                DroneModel.HEAVYWEIGHT,
                500,
                101,
                DroneState.LOADING,
                medicationList);
        Optional<String> errorMessage = droneValidationService.validate(drone);
        assertTrue(errorMessage.isPresent());
        assertEquals(String.format(DroneValidationService.BATTERY_LIMIT_ERROR, drone.getSerialNumber()), errorMessage.get());
    }

    @Test
    void validate() {

        List<Medication> medicationList = List.of(new Medication(
                1L,
                "paracetamol",
                99,
                "PCTML-500", "http://example.com"));
        Drone drone = new Drone(
                1L,
                "zephyr-15",
                DroneModel.HEAVYWEIGHT,
                500,
                99,
                DroneState.IDLE,
                medicationList);
        Optional<String> errorMessage = droneValidationService.validate(drone);
        assertFalse(errorMessage.isPresent());

    }
}