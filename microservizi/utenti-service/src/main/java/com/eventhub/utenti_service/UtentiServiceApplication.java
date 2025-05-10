package com.eventhub.utenti_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan({ "com.eventhub.utenti_service.entities" })
public class UtentiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtentiServiceApplication.class, args);
	}

}
