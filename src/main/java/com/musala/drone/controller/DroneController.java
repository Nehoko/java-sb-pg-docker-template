package com.musala.drone.controller;

import com.musala.drone.entity.Drone;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DroneController {

    @PostMapping(name = "/register-drone", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Drone registerDrone(@RequestBody Drone drone) {
        return drone;
    }

}
