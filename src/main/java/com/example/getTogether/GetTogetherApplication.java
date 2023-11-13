package com.example.getTogether;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GetTogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetTogetherApplication.class, args);
	}

}
