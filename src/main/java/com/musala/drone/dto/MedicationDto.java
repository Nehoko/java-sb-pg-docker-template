package com.musala.drone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MedicationDto implements Serializable {
    private Long id;
    private String name;
    private Integer weight;
    private String code;
    private String picture;
}
