package com.example.campsite;

import com.example.campsite.entity.Campsite;
import com.example.campsite.entity.Reservation;
import com.example.campsite.service.CampsiteService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class CampsiteApplicationTests {
    @Autowired
    private CampsiteService service;

    @Test
    public void testConcurrencyAccess() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Campsite> allCampsites = service.getAllCampsites();

        Callable<ResponseEntity> reserveTask = () -> {
            Reservation reservation1 = new Reservation();
            LocalDate now = LocalDate.now();
            reservation1.setCampsite(allCampsites.get(0));
            reservation1.setArrivalDate(now.plusDays(1));
            reservation1.setDepartureDate(now.plusDays(3));
            reservation1.setEmail("aaa@gmail.com");
            reservation1.setFullName("aaa");
            return service.reserveCampsite(reservation1);
        };
        for (int i = 0; i < 5; i++) {
            Future<ResponseEntity> submit = executorService.submit(reserveTask);
            log.info("Task is done? :" + submit.isDone());
            try {
                ResponseEntity responseEntity = submit.get();
                log.info(responseEntity.getStatusCode());
                log.info(responseEntity.getBody().toString());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (ExecutionException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Test
    public void testSingleAccess() {
        List<Campsite> allCampsites = service.getAllCampsites();
        allCampsites.forEach(c -> log.info(c.getAvailableDate()));
        Reservation reservation1 = new Reservation();

        LocalDate now = LocalDate.now();
        reservation1.setCampsite(allCampsites.get(0));
        reservation1.setArrivalDate(now.plusDays(1));
        reservation1.setDepartureDate(now.plusDays(3));
        reservation1.setEmail("aaa@gmail.com");
        reservation1.setFullName("aaa");

        ResponseEntity responseEntity = service.reserveCampsite(reservation1);

        log.info("Reserve result: " + responseEntity.getStatusCode());

        ResponseEntity reservation = service.getAllReservation();
        Object body = reservation.getBody();
        log.info(body);

    }

}
