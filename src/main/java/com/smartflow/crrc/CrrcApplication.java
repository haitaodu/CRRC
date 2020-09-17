package com.smartflow.crrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author haita
 */
@EnableScheduling
@SpringBootApplication
public class CrrcApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrrcApplication.class, args);
    }

}
