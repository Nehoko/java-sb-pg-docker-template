package com.musala.drone.mapper;

import com.musala.drone.dto.MedicationDto;
import com.musala.drone.entity.Medication;
import org.springframework.stereotype.Service;

@Service
public class MedicationMapper implements Mapper<Medication, MedicationDto> {

    @Override
    public MedicationDto toDto(Medication medication) {
        return new MedicationDto(
                medication.getId(),
                medication.getName(),
                medication.getWeight(),
                medication.getCode(),
                medication.getPicture());
    }

    @Override
    public Medication toEntity(MedicationDto medicationDto) {
        return new Medication(
                medicationDto.getId(),
                medicationDto.getName(),
                medicationDto.getWeight(),
                medicationDto.getCode(),
                medicationDto.getPicture());
    }
}
