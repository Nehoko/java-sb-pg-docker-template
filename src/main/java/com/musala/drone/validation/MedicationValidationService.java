package com.musala.drone.validation;

import com.musala.drone.entity.Medication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicationValidationService {

    public boolean validateName(Medication medication) {
        if (medication == null) {
            return true;
        }

        return medication.getName() != null
                && !medication.getName().isEmpty()
                && !medication.getName().isBlank()
                && medication.getName().matches("^[a-zA-Z0-9\\-_]+$");
    }

    public boolean validateCode(Medication medication) {
        if (medication == null) {
            return true;
        }

        return medication.getCode() != null
                && !medication.getCode().isEmpty()
                && !medication.getCode().isBlank()
                && medication.getCode().matches("^[A-Z0-9_]+$");
    }

    public Optional<String> validateMedication(Medication medication) {
        boolean isNameValid = this.validateName(medication);
        boolean isCodeValid = this.validateCode(medication);

        if (!isNameValid) {
            return Optional.of(String.format("Medication name %s invalid. Must contains letters, numbers, \"-\" and \"_\"", medication.getName()));
        }

        if (!isCodeValid) {
            return Optional.of(String.format("Medication code %s invalid. Must contains upper case letters, underscore and numbers.", medication.getName()));
        }

        return Optional.empty();
    }
}
