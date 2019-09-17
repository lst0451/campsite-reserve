package com.example.campsite.controller;

import com.example.campsite.entity.Reservation;
import com.example.campsite.service.CampsiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final CampsiteService service;

    public ReservationController(CampsiteService service) {
        this.service = service;
    }

    @PostMapping("/reservation")
    public ResponseEntity reserveCampsite(@RequestBody Reservation reservation) {
        return service.reserveCampsite(reservation);
    }

    @GetMapping("/reservations")
    public ResponseEntity getAllReservation() {
        return service.getAllReservation();
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity getReservation(@PathVariable String id) {
        return service.getReservationById(id);
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity cancelReservation(@PathVariable String id) {
        return service.deleteReservationById(id);
    }

}
