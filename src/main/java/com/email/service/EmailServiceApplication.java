package com.email.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
@EntityScan(basePackages = { "com.email.service.domain.model" })
@Slf4j
public class EmailServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Instant now = Instant.now();
		log.info("----------> Started on " + ZonedDateTime.ofInstant(now, ZoneId.systemDefault()).toOffsetDateTime());
		log.info("Default Timezone: " + ZoneId.systemDefault() + " OffsetDateTime:" + OffsetDateTime.now());
		log.info("UTC Time: " + ZonedDateTime.ofInstant(now, ZoneId.of("UTC")));
		log.info("São Paulo: " + ZonedDateTime.ofInstant(now, ZoneId.of("America/Sao_Paulo")));
		log.info("Java-Name:{}", System.getProperty("java.specification.name"));
		log.info("Java-Vendor:{}", System.getProperty("java.specification.vendor"));
		log.info("Java-Version:{}", System.getProperty("java.specification.version"));
		log.info("Java-Runtime-Version:{}", System.getProperty("java.runtime.version"));

	}


}
