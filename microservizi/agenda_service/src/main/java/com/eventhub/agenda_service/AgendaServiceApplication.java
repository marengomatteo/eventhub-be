package com.eventhub.agenda_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({ "com.eventhub.agenda_service.entities" })
public class AgendaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaServiceApplication.class, args);
	}

}
