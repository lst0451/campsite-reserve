package com.example.campsite.service;

import com.example.campsite.entity.Campsite;
import com.example.campsite.repository.CampsiteRepository;
import org.springframework.stereotype.Service;

@Service
public class CampsiteService {

    private final CampsiteRepository campsiteRepository;

    public CampsiteService(CampsiteRepository campsiteRepository) {
        this.campsiteRepository = campsiteRepository;
    }
    public void init() {
        Campsite campsite = new Campsite();
        campsite.setName("Montreal Island");
        campsiteRepository.save(campsite);
    }

}
