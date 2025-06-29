package com.eventhub.agenda_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AgendaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaServiceApplication.class, args);
	}

}
