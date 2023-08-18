package com.ms_procedure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class MsProcedureApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProcedureApplication.class, args);
	}

}
