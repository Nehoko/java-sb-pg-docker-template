package com.musala.drone.controller;

import com.musala.drone.dto.DroneDto;
import com.musala.drone.dto.MedicationDto;
import com.musala.drone.service.DroneService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping(path = "ping")
    public String ping() {
        return "Pong";
    }

    @PostMapping(path = "drone/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerDrone(@RequestBody DroneDto drone) {
        droneService.registerDrone(drone);
    }

    @PostMapping(path = "drone/{droneSerialNumber}/load", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void loadDrone(@RequestBody List<MedicationDto> medicationList, @PathVariable String droneSerialNumber) {
        droneService.loadDrone(medicationList, droneSerialNumber);
    }

    @GetMapping(path = "drone/{droneSerialNumber}/medications", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MedicationDto> getMedications(@PathVariable String droneSerialNumber) {
        return droneService.getMedications(droneSerialNumber);
    }

    @GetMapping(path = "drone/{droneSerialNumber}/battery", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getBatteryLevel(@PathVariable String droneSerialNumber) {
        return droneService.getBatteryLevel(droneSerialNumber);
    }

    @GetMapping(path = "drone/all-available-for-loading-drones", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DroneDto> getAllAvailableForLoadingDrones() {
        //checking available drones for loading
        return droneService.getAllAvailableForLoadingDrones();
    }

}
