package com.musala.drone.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "medication")
@Table(name = "medication")
@Getter
@Setter
@ToString
@FieldNameConstants
public class Medication {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String picture;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Drone drone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
