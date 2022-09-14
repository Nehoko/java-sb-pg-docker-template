package com.musala.drone.validation;

import com.musala.drone.entity.Drone;
import com.musala.drone.entity.Medication;
import com.musala.drone.enums.drone.DroneState;
import org.springframework.stereotype.Service;

/**
 * Prevent the drone from being loaded with more weight that it can carry;
 * Prevent the drone from being in LOADING state if the battery level is below 25%;
 */

@Service
public class DroneValidationService {

    public static final int BATTERY_CAPACITY = 25;

    /**
     * Prevent the drone from being loaded with more weight that it can carry;
     * @param drone drone
     * @return is it valid
     */
    public boolean validateWeight(Drone drone) {

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
    public boolean validateState(Drone drone) {
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
        boolean batteryLevelIsBelow = 25 > batteryCapacity;

        return !(stateIsLoading && batteryLevelIsBelow);
    }
}
