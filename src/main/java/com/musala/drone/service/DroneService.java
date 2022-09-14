package com.musala.drone.service;

import com.musala.drone.dto.DroneDto;
import com.musala.drone.dto.MedicationDto;
import com.musala.drone.entity.Drone;
import com.musala.drone.entity.Medication;
import com.musala.drone.enums.drone.DroneState;
import com.musala.drone.mapper.DroneMapper;
import com.musala.drone.mapper.MedicationMapper;
import com.musala.drone.repository.DroneRepository;
import com.musala.drone.validation.DroneValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;
    private final DroneValidationService droneValidationService;

    public DroneService(DroneRepository droneRepository,
                        DroneMapper droneMapper,
                        MedicationMapper medicationMapper, DroneValidationService droneValidationService) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
        this.medicationMapper = medicationMapper;
        this.droneValidationService = droneValidationService;
    }

    @Transactional
    public void registerDrone(DroneDto droneDto) {
        Drone drone = droneMapper.toEntity(droneDto);
        validateAndSave(drone);
    }

    @Transactional
    public void loadDrone(List<MedicationDto> medicationDtoList, String droneSerialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumber(droneSerialNumber)
                .orElseThrow(() -> new RuntimeException(String.format("drone with serial number: %s has not found", droneSerialNumber)));

        List<Medication> medicationList = medicationMapper.toEntityList(medicationDtoList);

        drone.addAllMedications(medicationList);

        validateAndSave(drone);

    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getMedications(String droneSerialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumber(droneSerialNumber)
                .orElseThrow(() -> new RuntimeException(String.format("drone with serial number: %s has not found", droneSerialNumber)));

        return medicationMapper.toDtoList(drone.getMedicationList());

    }

    @Transactional(readOnly = true)
    public int getBatteryLevel(String droneSerialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumber(droneSerialNumber)
                .orElseThrow(() -> new RuntimeException(String.format("drone with serial number: %s has not found", droneSerialNumber)));

        return drone.getBatteryCapacity();
    }

    @Transactional(readOnly = true)
    public List<DroneDto> getAllAvailableForLoadingDrones() {
        List<Drone> availableDroneList = droneRepository
                .findAllByStateIsInAndBatteryCapacityGreaterThanEqual(
                        List.of(DroneState.IDLE, DroneState.LOADING),
                        DroneValidationService.BATTERY_CAPACITY
                );

        if (availableDroneList == null || availableDroneList.isEmpty()) {
            return new ArrayList<>();
        }

        return droneMapper.toDtoList(availableDroneList);
    }

    private void validateAndSave(Drone drone) {
        boolean stateIsValid = droneValidationService.validateState(drone);
        boolean weightIsValid = droneValidationService.validateWeight(drone);

        if (!stateIsValid) {
            throw new RuntimeException(String.format("drone with serial number: %s has low battery", drone.getSerialNumber()));
        }

        if (!weightIsValid) {
            throw new RuntimeException(String.format("drone with serial number: %s is overloaded", drone.getSerialNumber()));
        }

        droneRepository.save(drone);
    }
}
