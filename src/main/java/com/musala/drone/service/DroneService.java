package com.musala.drone.service;

import com.musala.drone.dto.DroneDto;
import com.musala.drone.dto.MedicationDto;
import com.musala.drone.entity.Drone;
import com.musala.drone.entity.Medication;
import com.musala.drone.enums.DroneState;
import com.musala.drone.mapper.DroneMapper;
import com.musala.drone.mapper.MedicationMapper;
import com.musala.drone.repository.DroneRepository;
import com.musala.drone.validation.DroneValidationService;
import com.musala.drone.validation.MedicationValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;
    private final DroneValidationService droneValidationService;
    private final MedicationValidationService medicationValidationService;

    public DroneService(DroneRepository droneRepository,
                        DroneMapper droneMapper,
                        MedicationMapper medicationMapper,
                        DroneValidationService droneValidationService,
                        MedicationValidationService medicationValidationService) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
        this.medicationMapper = medicationMapper;
        this.droneValidationService = droneValidationService;
        this.medicationValidationService = medicationValidationService;
    }

    @Transactional
    public void registerDrone(DroneDto droneDto) {
        Drone drone = droneMapper.toEntity(droneDto);
        validateAndSave(drone);
    }

    @Transactional
    public void loadDrone(List<MedicationDto> medicationDtoList, String droneSerialNumber) {

        if (medicationDtoList == null || medicationDtoList.isEmpty()) {
            return;
        }

        if (droneValidationService.validateSerialNumber(droneSerialNumber)) {
            throw new IllegalArgumentException(String.format("Invalid drone serial number: %s", droneSerialNumber));
        }

        Drone drone = droneRepository.findDroneBySerialNumber(droneSerialNumber)
                .orElseThrow(() -> new RuntimeException(String.format("Drone with serial number: %s has not found", droneSerialNumber)));

        List<Medication> medicationList = medicationMapper.toEntityList(medicationDtoList);

        drone.addAllMedications(medicationList);

        validateAndSave(drone);

    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getMedications(String droneSerialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumber(droneSerialNumber)
                .orElseThrow(() -> new RuntimeException(String.format("Drone with serial number: %s has not found", droneSerialNumber)));

        return medicationMapper.toDtoList(drone.getMedicationList());

    }

    @Transactional(readOnly = true)
    public int getBatteryLevel(String droneSerialNumber) {

        if (droneValidationService.validateSerialNumber(droneSerialNumber)) {
            throw new IllegalArgumentException(String.format("Invalid drone serial number: %s", droneSerialNumber));
        }

        Drone drone = droneRepository.findDroneBySerialNumber(droneSerialNumber)
                .orElseThrow(() -> new RuntimeException(String.format("Drone with serial number: %s has not found", droneSerialNumber)));

        return drone.getBatteryCapacity();
    }

    @Transactional(readOnly = true)
    public List<DroneDto> getAllAvailableForLoadingDrones() {
        List<Drone> availableDroneList = droneRepository
                .findAllByStateIsInAndBatteryCapacityGreaterThanEqual(
                        List.of(DroneState.IDLE, DroneState.LOADING),
                        DroneValidationService.MIN_BATTERY_CAPACITY
                );

        if (availableDroneList == null || availableDroneList.isEmpty()) {
            return new ArrayList<>();
        }

        return droneMapper.toDtoList(availableDroneList);
    }

    public Map<String, Integer> getAllSerialNumberAndBatteryLevel() {
        return droneRepository
                .findAll()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Drone::getSerialNumber, Drone::getBatteryCapacity));
    }

    private void validateAndSave(Drone drone) {
        Optional<String> droneError = droneValidationService.validate(drone);

        if (droneError.isPresent()) {
            throw new RuntimeException(droneError.get());
        }

        List<Medication> medicationList = drone.getMedicationList();

        if (medicationList != null && !medicationList.isEmpty()) {
            for (Medication medication : medicationList) {
                Optional<String> medicationError = medicationValidationService.validateMedication(medication);

                if (medicationError.isPresent()) {
                    throw new RuntimeException(medicationError.get());
                }
            }
        }

        droneRepository.save(drone);
    }
}
