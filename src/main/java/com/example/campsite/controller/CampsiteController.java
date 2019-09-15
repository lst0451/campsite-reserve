package com.example.campsite.controller;

import com.example.campsite.entity.Campsite;
import com.example.campsite.service.CampsiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CampsiteController {
    private final CampsiteService service;

    public CampsiteController(CampsiteService service) {
        this.service = service;
    }

    @GetMapping("/campsites")
    public ResponseEntity<?> getAllCampsites() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/campsite/{id}")
    public ResponseEntity getCampsiteById(@PathVariable Long id,
                                          @RequestParam(value = "fromDate", required = false) String fromDate,
                                          @RequestParam(value = "toDate", required = false) String toDate) {
        Optional<Campsite> campsite = service.getCampsiteAndAvailableDate(id, fromDate, toDate);
        if (campsite.isPresent()) {
            return ResponseEntity.ok(campsite);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
