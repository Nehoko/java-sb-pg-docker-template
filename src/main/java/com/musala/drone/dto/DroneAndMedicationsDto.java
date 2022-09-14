package com.musala.drone.dto;

import lombok.Data;

import java.util.List;

@Data
public class DroneAndMedicationsDto {
    private DroneDto drone;
    private List<MedicationDto> medicationsList;
}
