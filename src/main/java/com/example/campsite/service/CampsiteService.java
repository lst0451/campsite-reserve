package com.example.campsite.service;

import com.example.campsite.entity.Campsite;
import com.example.campsite.entity.Reservation;
import com.example.campsite.repository.CampsiteRepository;
import com.example.campsite.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CampsiteService {

    private final CampsiteRepository campsiteRepository;
    private final ReservationRepository reservationRepository;

    public CampsiteService(CampsiteRepository campsiteRepository,
                           ReservationRepository reservationRepository) {
        this.campsiteRepository = campsiteRepository;
        this.reservationRepository = reservationRepository;
    }

    public void init() {
        Campsite campsite = new Campsite();
        campsite.setName("Pacific Island");
        campsiteRepository.save(campsite);
    }

    public List<Campsite> getAllCampsites() {
        List<Campsite> all = campsiteRepository.findAll();
        all.forEach(c -> {
            processSingleCampsite(null, null, Optional.of(c));
        });
        return all;
    }

    public Optional<Campsite> getCampsiteAndAvailableDate(Long id, String fromDate, String toDate) {
        Optional<Campsite> campsite = campsiteRepository.findById(id);
        LocalDate from = null;
        LocalDate to = null;
        if (fromDate != null) {
            from = LocalDate.parse(fromDate);
        }
        if (toDate != null) {
            to = LocalDate.parse(toDate);
        }

        if (campsite.isPresent()) {
            processSingleCampsite(from, to, campsite);
        }
        return campsite;
    }

    private void processSingleCampsite(LocalDate fromDate, LocalDate toDate, Optional<Campsite> campsite) {
        List<Reservation> reservations = campsite.get().getReservations();
        Set<LocalDate> occupiedDate = new HashSet<>();
        for (Reservation r : reservations) {
            occupiedDate.addAll(r.getOccupiedDate());
        }
        Set<LocalDate> oneMonthDays = getOneMonthDays(fromDate, toDate);
        oneMonthDays.removeAll(occupiedDate);
        campsite.get().setAvailableDate(oneMonthDays);
    }

    public synchronized ResponseEntity reserveCampsite(Reservation reservation) {
        if (checkAvailability(reservation)) {
            Reservation savedReservation = reservationRepository.save(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean checkAvailability(Reservation reservation) {
        LocalDate arrivalDate = reservation.getArrivalDate();
        LocalDate departureDate = reservation.getDepartureDate();
        LocalDate now = LocalDate.now();
        if (arrivalDate == null || departureDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation date shouldn't be null");
        }
        if (!arrivalDate.isBefore(departureDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arrival date should be early than departure date.");
        }
        if (!arrivalDate.isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The campsite can be reserved minimum 1 day(s) ahead of arrival");
        }
        if (arrivalDate.isAfter(now.plusMonths(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The campsite can be reserved up to 1 month in advance.");
        }
        Campsite campsite = reservation.getCampsite();
        if (campsite == null) {
            throw new RuntimeException("Campsite id shouldn't be null");
        }
        int reserveDays = arrivalDate.until(departureDate).getDays();
        if (reserveDays > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The campsite can be reserved for max 3 days.");
        }
        Set<LocalDate> reserveDate = new LinkedHashSet<>();
        for (int i = 0; i < reserveDays; i++) {
            reserveDate.add(arrivalDate.plusDays(i));
        }
        Optional<Campsite> campsiteInDb = campsiteRepository.findById(campsite.getId());
        if (campsiteInDb.isPresent()) {
            processSingleCampsite(arrivalDate, departureDate, campsiteInDb);
            Set<LocalDate> availableDays = campsiteInDb.get().getAvailableDate();
            if (availableDays.size() < reserveDays) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping reservation days.");
            }
            if (availableDays.containsAll(reserveDate)) {
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping reservation days.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campsite doesn't exist.");
        }
    }

    private Set<LocalDate> getOneMonthDays(LocalDate fromDate, LocalDate toDate) {
        LocalDate from = null;
        LocalDate endDate = null;
        LocalDate now = LocalDate.now();
        if (fromDate != null) {
//            from = LocalDate.parse(fromDate);
            from = fromDate;
        } else {
            from = now.plusDays(1);
        }
        if (toDate != null) {
//            endDate = LocalDate.parse(toDate);
            endDate = toDate;
            LocalDate oneMonthLater = now.plusMonths(1);
            if (endDate.isAfter(oneMonthLater)) {
                endDate = oneMonthLater;
            }
        } else {
            endDate = now.plusMonths(1);
        }
        Set<LocalDate> days = new LinkedHashSet<>();
        long amount = from.until(endDate, ChronoUnit.DAYS);
        for (int i = 0; i < amount; i++) {
            days.add(from.plusDays(i));
        }
        return days;
    }

    public ResponseEntity getReservationById(String id) {
        Optional<Reservation> byId = reservationRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity deleteReservationById(String id) {
        reservationRepository.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity getAllReservation() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }
}
