package com.eventhub.mail_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan({ "com.eventhub.mail_service.entities" })
public class MailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}

}
