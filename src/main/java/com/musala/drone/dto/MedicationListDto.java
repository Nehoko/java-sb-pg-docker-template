package com.musala.drone.dto;

import lombok.Data;

import java.util.List;

@Data
public class MedicationListDto {
    private List<MedicationDto> medicationList;
}
