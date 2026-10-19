package com.prakash.gmcemp.emission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EmissionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                EmissionServiceApplication.class,
                args);
    }

}