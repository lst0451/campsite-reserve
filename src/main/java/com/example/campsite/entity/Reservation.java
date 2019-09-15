package com.example.campsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
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
    private Set<LocalDate> occupiedDate;

    public Set<LocalDate> getOccupiedDate() {
        Period period = Period.between(arrivalDate, departureDate);
        for (int i = 0; i < period.getDays(); i++) {
            occupiedDate.add(arrivalDate.plusDays(i));
        }
        return occupiedDate;
    }
}
