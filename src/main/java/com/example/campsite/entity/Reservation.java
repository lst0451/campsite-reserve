package com.example.campsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

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
    private Set<LocalDate> occupiedDate = new LinkedHashSet<>();

    public Set<LocalDate> getOccupiedDate() {
        long until = arrivalDate.until(departureDate, ChronoUnit.DAYS);
        for (int i = 0; i < until; i++) {
            occupiedDate.add(arrivalDate.plusDays(i));
        }
        return occupiedDate;
    }
}
