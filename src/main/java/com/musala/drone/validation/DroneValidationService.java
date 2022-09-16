package com.musala.drone.validation;

import com.musala.drone.entity.Drone;
import com.musala.drone.entity.Medication;
import com.musala.drone.enums.DroneState;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Prevent the drone from being loaded with more weight that it can carry;
 * Prevent the drone from being in LOADING state if the battery level is below 25%;
 */

@Service
public class DroneValidationService {

    public static final int MIN_BATTERY_CAPACITY = 25;
    public static final int MAX_BATTERY_CAPACITY = 100;
    public static final int MAX_WEIGHT_LIMIT = 500;
    public static final String INVALID_SERIAL_NUMBER_ERROR = "Invalid serial number: %s";
    public static final String LOW_BATTERY_ERROR = "Drone with serial number: %s has low battery";
    public static final String OVERLOAD_ERROR = "Drone with serial number: %s is overloaded";
    public static final String WEIGHT_LIMIT_ERROR = "Drone with serial number: %s has weight limit more than MAX (500g)";
    public static final String BATTERY_LIMIT_ERROR = "Drone with serial number: %s has battery capacity more than MAX (100)";

    /**
     * Prevent the drone from being loaded with more weight that it can carry;
     * @param drone drone
     * @return is it valid
     */
    private boolean validateWeight(Drone drone) {

        if (drone == null) {
            return false;
        }

        if (drone.getMedicationList() == null || drone.getMedicationList().isEmpty()) {
            return true;
        }

        Integer weightLimit = drone.getWeightLimit();

        if (weightLimit == null) {
            weightLimit = 0;
        }

        Integer actualWeight = drone.getMedicationList().stream().map(Medication::getWeight).reduce(0, Integer::sum);

        return weightLimit >= actualWeight;
    }

    /**
     * Prevent the drone from being in LOADING state if the battery level is below 25%;
     * @param drone drone
     * @return is it valid
     */
    private boolean validateState(Drone drone) {
        if (drone == null) {
            return false;
        }

        if (drone.getState() == null) {
            return true;
        }

        Integer batteryCapacity = drone.getBatteryCapacity();

        if (batteryCapacity == null) {
            batteryCapacity = 0;
        }

        boolean stateIsLoading = DroneState.LOADING.equals(drone.getState());
        boolean batteryLevelIsBelow = MIN_BATTERY_CAPACITY > batteryCapacity;

        return !(stateIsLoading && batteryLevelIsBelow);
    }

    public boolean validateSerialNumber(String droneSerialNumber) {
        return droneSerialNumber!=null
                && !droneSerialNumber.isEmpty()
                && !droneSerialNumber.isBlank()
                && !(droneSerialNumber.length() > 100);
    }

    private boolean validateWeightLimit(Drone drone) {
        if (drone == null || drone.getWeightLimit() == null || drone.getWeightLimit() == 0) return true;

        return MAX_WEIGHT_LIMIT >= drone.getWeightLimit();
    }

    private boolean validateBatteryCapacity(Drone drone) {
        if (drone == null || drone.getBatteryCapacity() == null || drone.getBatteryCapacity() == 0) return true;

        return MAX_BATTERY_CAPACITY >= drone.getBatteryCapacity();

    }

    public Optional<String> validate(Drone drone) {
        boolean serialNumberIsValid = this.validateSerialNumber(drone.getSerialNumber());
        boolean stateIsValid = this.validateState(drone);
        boolean weightIsValid = this.validateWeight(drone);
        boolean maxWeightIsValid = this.validateWeightLimit(drone);
        boolean batteryCapacityIsValid = this.validateBatteryCapacity(drone);

        if (!serialNumberIsValid) {
            return Optional.of(String.format(INVALID_SERIAL_NUMBER_ERROR, drone.getSerialNumber()));
        }

        if (!stateIsValid) {
            return Optional.of(String.format(LOW_BATTERY_ERROR, drone.getSerialNumber()));
        }

        if (!weightIsValid) {
            return Optional.of(String.format(OVERLOAD_ERROR, drone.getSerialNumber()));
        }

        if (!maxWeightIsValid) {
            return Optional.of(String.format(WEIGHT_LIMIT_ERROR, drone.getSerialNumber()));
        }

        if (!batteryCapacityIsValid) {
            return Optional.of(String.format(BATTERY_LIMIT_ERROR, drone.getSerialNumber()));
        }

        return Optional.empty();
    }
}
