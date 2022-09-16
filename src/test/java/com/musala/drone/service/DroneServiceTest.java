package com.musala.drone.service;

import com.musala.drone.dto.DroneDto;
import com.musala.drone.dto.MedicationDto;
import com.musala.drone.entity.Drone;
import com.musala.drone.enums.DroneModel;
import com.musala.drone.enums.DroneState;
import com.musala.drone.mapper.DroneMapper;
import com.musala.drone.repository.DroneRepository;
import com.musala.drone.validation.DroneValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class DroneServiceTest {


    @MockBean
    private DroneRepository droneRepository;

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneMapper droneMapper;

    @Test
    void registerDrone() {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>(1));
        Drone drone = droneMapper.toEntity(dto);
        when(droneRepository.save(drone)).thenReturn(drone);
        assertDoesNotThrow(() -> droneService.registerDrone(dto));
    }

    @Test
    void loadDrone() {
        ArrayList<MedicationDto> medicationList = new ArrayList<>(1);
        medicationList.add(new MedicationDto(
                1L,
                "ibuprofen",
                5,
                "IBU_5",
                "http://example.com"
        ));

        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                medicationList);
        Drone drone = droneMapper.toEntity(dto);
        when(droneRepository.save(drone)).thenReturn(drone);
        when(droneRepository.findDroneBySerialNumber(dto.getSerialNumber())).thenReturn(Optional.of(drone));
        assertDoesNotThrow(() -> droneService.loadDrone(medicationList, dto.getSerialNumber()));
    }

    @Test
    void getMedications() {
        ArrayList<MedicationDto> medicationList = new ArrayList<>(1);
        medicationList.add(new MedicationDto(
                1L,
                "ibuprofen",
                5,
                "IBU_5",
                "http://example.com"
        ));

        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                medicationList);
        Drone drone = droneMapper.toEntity(dto);
        when(droneRepository.findDroneBySerialNumber(dto.getSerialNumber())).thenReturn(Optional.of(drone));
        assertEquals(medicationList, assertDoesNotThrow(() -> droneService.getMedications(dto.getSerialNumber())));
    }

    @Test
    void getBatteryLevel() {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>());
        Drone drone = droneMapper.toEntity(dto);
        when(droneRepository.findDroneBySerialNumber(dto.getSerialNumber())).thenReturn(Optional.of(drone));
        assertEquals(dto.getBatteryCapacity(), assertDoesNotThrow(() -> droneService.getBatteryLevel(dto.getSerialNumber())));
    }

    @Test
    void getAllAvailableForLoadingDrones() {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>());
        Drone drone = droneMapper.toEntity(dto);
        List<DroneDto> dtoList = List.of(dto);
        List<Drone> droneList = List.of(drone);
        when(droneRepository
                .findAllByStateIsInAndBatteryCapacityGreaterThanEqual(
                        List.of(DroneState.IDLE, DroneState.LOADING),
                        DroneValidationService.MIN_BATTERY_CAPACITY
                )).thenReturn(droneList);
        assertEquals(dtoList, assertDoesNotThrow(() -> droneService.getAllAvailableForLoadingDrones()));
    }

    @Test
    void getAllSerialNumberAndBatteryLevel() {
        DroneDto dto = new DroneDto(
                1L,
                "zephyr-15",
                DroneModel.LIGHTWEIGHT,
                100,
                99,
                DroneState.IDLE,
                new ArrayList<>());
        Drone drone = droneMapper.toEntity(dto);
        Map<String, Integer> serialNumberAndBatteryLevel = Map.of(dto.getSerialNumber(), dto.getBatteryCapacity());
        when(droneRepository.findAll()).thenReturn(List.of(drone));
        assertEquals(serialNumberAndBatteryLevel, assertDoesNotThrow(() -> droneService.getAllSerialNumberAndBatteryLevel()));
    }
}