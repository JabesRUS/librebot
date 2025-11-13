package com.jabes.librebot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //  включает механизм автоматического заполнения временных меток в Spring
@Slf4j
public class LibrebotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrebotApplication.class, args);

	}



}
