package com.jabes.librebot;

import com.jabes.librebot.service.LibreViewAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //  включает механизм автоматического заполнения временных меток в Spring
@Slf4j
public class LibrebotApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
	public static void main(String[] args) {
		SpringApplication.run(LibrebotApplication.class, args);

	}

    /**
     * Тестовый метод для проверки авторизации LibreView API.
     * Выполняется автоматически при старте приложения.
     *
     * ВРЕМЕННЫЙ КОД - удалить после проверки!
     */
    @Bean
    public CommandLineRunner testLibreViewAuth(LibreViewAuthService authService) {
        return args -> {
            log.info("========================================");
            log.info("ТЕСТ: Получение токена LibreView API");
            log.info("========================================");

            try {
                String token = authService.getValidToken();

                log.info("✅ УСПЕХ! Токен получен:");
                log.info("Токен (первые 50 символов): {}", token.substring(0, Math.min(50, token.length())) + "...");
                log.info("Длина токена: {} символов", token.length());
            } catch (Exception e) {
                log.error("❌ ОШИБКА при получении токена!", e);
            }
            log.info("========================================");
        };
    }

}
