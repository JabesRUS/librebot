package com.jabes.librebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //  включает механизм автоматического заполнения временных меток в Spring
public class LibrebotApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
	public static void main(String[] args) {
		SpringApplication.run(LibrebotApplication.class, args);
	}

}
