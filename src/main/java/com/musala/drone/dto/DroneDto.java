package com.musala.drone.dto;


import com.musala.drone.enums.DroneModel;
import com.musala.drone.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class DroneDto implements Serializable {

    private Long id;

    private String serialNumber;

    private DroneModel model;

    private Integer weightLimit;

    private Integer batteryCapacity;

    private DroneState state;

    private List<MedicationDto> medicationList;
}
