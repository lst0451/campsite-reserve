package com.example.campsite.service;

import com.example.campsite.entity.Campsite;
import com.example.campsite.entity.Reservation;
import com.example.campsite.repository.CampsiteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CampsiteService {

    private final CampsiteRepository campsiteRepository;

    public CampsiteService(CampsiteRepository campsiteRepository) {
        this.campsiteRepository = campsiteRepository;
    }

    public void init() {
        Campsite campsite = new Campsite();
        campsite.setName("Pacific Island");
        campsiteRepository.save(campsite);
    }

    public List<Campsite> getAllCampsites(){
        List<Campsite> all = campsiteRepository.findAll();
        all.forEach(c->{
            processSingleCampsite(null,null,Optional.of(c));
        });
        return all;
    }

    public Optional<Campsite> getCampsiteAndAvailableDate(Long id, String fromDate, String toDate) {
        Optional<Campsite> campsite = campsiteRepository.findById(id);
        if (campsite.isPresent()) {
            processSingleCampsite(fromDate, toDate, campsite);
        }
        return campsite;
    }

    private void processSingleCampsite(String fromDate, String toDate, Optional<Campsite> campsite) {
        List<Reservation> reservations = campsite.get().getReservations();
        Set<LocalDate> occupiedDate = new HashSet<>();
        for (Reservation r : reservations) {
            occupiedDate.addAll(r.getOccupiedDate());
        }
        Set<LocalDate> oneMonthDays = getOneMonthDays(fromDate, toDate);
        oneMonthDays.removeAll(occupiedDate);
        campsite.get().setAvailableDays(oneMonthDays);
    }

    public static Set<LocalDate> getOneMonthDays(String fromDate, String toDate) {
        LocalDate from = null;
        LocalDate endDate = null;
        if (fromDate != null) {
            from = LocalDate.parse(fromDate);
        } else {
            from = LocalDate.now().plusDays(1);
        }
        if (toDate != null) {
            endDate = LocalDate.parse(toDate);
            LocalDate oneMonthLater = LocalDate.now().plusMonths(1);
            if (oneMonthLater.compareTo(endDate) < 0) {
                endDate = oneMonthLater;
            }
        } else {
            endDate = LocalDate.now().plusMonths(1);
        }
        Set<LocalDate> days = new LinkedHashSet<>();
        long amount = from.until(endDate, ChronoUnit.DAYS);
        for (int i = 0; i < amount; i++) {
            days.add(from.plusDays(i));
        }
        return days;
    }
}
