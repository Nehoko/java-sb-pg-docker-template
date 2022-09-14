package com.musala.drone.scheduled;

import com.musala.drone.service.DroneService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class DronesBatteryJob {

    private final DroneService droneService;

    public DronesBatteryJob(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void checkBatteryLevelsAndLogIt() {
        Map<String, Integer> serialNumberBatteryMap = droneService.getAllSerialNumberAndBatteryLevel();
        if (serialNumberBatteryMap.isEmpty()) {
            log.info("There are no drones at the database yet.");
        }
        serialNumberBatteryMap.forEach((key, value) -> log.info("Drone serial number: {} battery: {}", key, value));
    }
}
