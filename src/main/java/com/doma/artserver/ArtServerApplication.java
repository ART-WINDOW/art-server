package com.doma.artserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtServerApplication.class, args);
    }

}
