package com.example.campsite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"reservations"})
public class Campsite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ApiModelProperty(hidden = true)
    private String name;

    @Transient
    @ApiModelProperty(hidden = true)
    private Set<LocalDate> availableDate;

    @OneToMany(mappedBy = "campsite", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Reservation> reservations;


}
