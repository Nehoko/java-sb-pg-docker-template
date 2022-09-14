package com.musala.drone.mapper;

import com.musala.drone.dto.DroneDto;
import com.musala.drone.dto.MedicationDto;
import com.musala.drone.entity.Drone;
import com.musala.drone.entity.Medication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneMapper implements Mapper<Drone, DroneDto> {

    private final MedicationMapper medicationMapper;

    public DroneMapper(MedicationMapper medicationMapper) {
        this.medicationMapper = medicationMapper;
    }

    @Override
    public DroneDto toDto(Drone drone) {
        return new DroneDto(
                drone.getId(),
                drone.getSerialNumber(),
                drone.getModel(),
                drone.getWeightLimit(),
                drone.getBatteryCapacity(),
                drone.getState(),
                medicationMapper.toDtoList(drone.getMedicationList())
        );
    }

    @Override
    public Drone toEntity(DroneDto droneDto) {

        Drone drone = new Drone(
                droneDto.getId(),
                droneDto.getSerialNumber(),
                droneDto.getModel(),
                droneDto.getWeightLimit(),
                droneDto.getBatteryCapacity(),
                droneDto.getState(),
                new ArrayList<>()
        );
        if (droneDto.getMedicationList() != null && !droneDto.getMedicationList().isEmpty()) {

            List<Medication> medicationList = new ArrayList<>(droneDto.getMedicationList().size());

            for (MedicationDto dto : droneDto.getMedicationList()) {
                Medication medication = medicationMapper.toEntity(dto);
                medication.setDrone(drone);
                medicationList.add(medication);
            }
            drone.setMedicationList(medicationList);
        }


        return drone;
    }
}
