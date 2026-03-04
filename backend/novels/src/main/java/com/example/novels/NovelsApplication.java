package com.example.novels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class NovelsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NovelsApplication.class, args);
	}

}
