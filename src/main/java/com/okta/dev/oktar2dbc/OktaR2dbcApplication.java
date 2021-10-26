package com.okta.dev.oktar2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableScheduling
@EnableWebFlux
@EnableR2dbcRepositories
@SpringBootApplication
public class OktaR2dbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(OktaR2dbcApplication.class, args);
    }

}
