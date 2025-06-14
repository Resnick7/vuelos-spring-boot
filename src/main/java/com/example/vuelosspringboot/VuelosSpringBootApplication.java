package com.example.vuelosspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VuelosSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(VuelosSpringBootApplication.class, args);
    }

}
