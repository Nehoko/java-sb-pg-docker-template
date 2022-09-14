package com.musala.drone.entity;

import com.musala.drone.enums.drone.DroneModel;
import com.musala.drone.enums.drone.DroneState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "drone")
@Table(name = "drone")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Drone {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private DroneModel model;

    @Column(nullable = false)
    private Integer weightLimit;

    @Column(nullable = false)
    private Integer batteryCapacity;

    @Column(nullable = false)
    private DroneState state;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medicationList;

    public Drone() {

    }

    public void addMedication(Medication medication) {
        this.medicationList.add(medication);
        medication.setDrone(this);
    }

    public void removeMedication(Medication medication) {
        this.medicationList.remove(medication);
        medication.setDrone(null);
    }

    public void addAllMedications(List<Medication> medicationList) {
        for (Medication medication : medicationList) {
            addMedication(medication);
        }
    }

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
