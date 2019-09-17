package com.example.campsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Reservation {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "campsite_id")
    @JsonBackReference
    private Campsite campsite;

    private String email;
    private String fullName;
    @Column(nullable = false)
    private LocalDate arrivalDate;
    @Column(nullable = false)
    private LocalDate departureDate;

    @Transient
    @JsonProperty(value = "reservedDate")
    private Set<LocalDate> occupiedDate = new LinkedHashSet<>();

    public Set<LocalDate> getOccupiedDate() {
        long until = arrivalDate.until(departureDate, ChronoUnit.DAYS);
        for (int i = 0; i < until; i++) {
            occupiedDate.add(arrivalDate.plusDays(i));
        }
        return occupiedDate;
    }
}
