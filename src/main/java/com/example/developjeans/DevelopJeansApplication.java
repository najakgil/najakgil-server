package com.example.developjeans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevelopJeansApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevelopJeansApplication.class, args);
    }

}
