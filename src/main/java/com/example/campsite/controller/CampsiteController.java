package com.example.campsite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CampsiteController {
    @GetMapping("/campsites")
    public ResponseEntity<?> getAllCampsites() {
        return ResponseEntity.ok(null);
    }
}
