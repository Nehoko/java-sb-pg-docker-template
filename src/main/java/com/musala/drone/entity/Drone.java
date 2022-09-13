package com.musala.drone.entity;

import com.musala.drone.enums.drone.DroneModel;
import com.musala.drone.enums.drone.DroneState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Drone {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String serialNumber;

    @Column(nullable = false)
    private DroneModel model;

    @Column(nullable = false)
    private Integer weightLimit;

    @Column(nullable = false)
    private Integer batteryCapacity;

    @Column(nullable = false)
    private DroneState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drone drone = (Drone) o;
        return id.equals(drone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
