package com.example.campsite;

import com.example.campsite.service.CampsiteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CampsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampsiteApplication.class, args);
    }

    @Bean
    CommandLineRunner init(CampsiteService service) {
        return (args) -> {
            service.init();
        };
    }

}
