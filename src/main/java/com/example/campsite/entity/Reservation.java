package com.example.campsite.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JsonBackReference
    private Campsite campsite;

    private String email;
    private String fullName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;

}
