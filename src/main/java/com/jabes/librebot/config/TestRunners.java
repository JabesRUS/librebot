package com.jabes.librebot.config;

import com.jabes.librebot.model.dto.libreview.GlucoseMeasurementDto;
import com.jabes.librebot.model.dto.libreview.LibreViewConnection;
import com.jabes.librebot.service.LibreViewApiService;
import com.jabes.librebot.service.LibreViewAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@Profile("dev")  // ← Выполнять только в dev профиле!
@Slf4j
public class TestRunners {

    @Bean
    @Order(1)  // ← Выполнить первым
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

    @Bean
    @Order(2)
    public CommandLineRunner testLibreViewConnections(LibreViewApiService apiService) {
        return args -> {
            log.info("========================================");
            log.info("ТЕСТ: Получение списка подключений");
            log.info("========================================");

            try {
                List<LibreViewConnection> connections = apiService.getConnections();

                log.info("✅ УСПЕХ! Получено подключений: {}", connections.size());

                // Показать информацию о каждом подключении
                for (int i = 0; i < connections.size(); i++) {
                    LibreViewConnection conn = connections.get(i);
                    log.info("Подключение #{}: {} {} (patientId: {})",
                            i + 1,
                            conn.getFirstName(),
                            conn.getLastName(),
                            conn.getPatientId());

                    // Если есть последнее измерение - показать
                    if (conn.getGlucoseMeasurement() != null) {
                        GlucoseMeasurementDto measurement = conn.getGlucoseMeasurement();
                        log.info("  └─ Последнее измерение: {} ммоль/л ({})",
                                measurement.getValueInMmol(),
                                measurement.getTimeStamp());
                    }
                }

            } catch (Exception e) {
                log.error("❌ ОШИБКА при получении подключений!", e);
            }

            log.info("========================================");
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner testLibreViewGlucoseData(LibreViewApiService apiService) {
        return args -> {
            log.info("========================================");
            log.info("ТЕСТ: Получение данных глюкозы");
            log.info("========================================");

            try {
                // Сначала получить connections чтобы взять patientId
                List<LibreViewConnection> connections = apiService.getConnections();

                if (connections == null || connections.isEmpty()) {
                    log.warn("⚠️ Нет доступных подключений для теста");
                    return;
                }

                // Взять первого пациента
                String patientId = connections.get(0).getPatientId();
                String patientName = connections.get(0).getFirstName() + " " +
                        connections.get(0).getLastName();

                log.info("Запрос данных для пациента: {} (ID: {})", patientName, patientId);

                // Получить данные глюкозы
                List<GlucoseMeasurementDto> glucoseData = apiService.getGlucoseData(patientId);

                log.info("✅ УСПЕХ! Получено измерений: {}", glucoseData.size());

                if (glucoseData.isEmpty()) {
                    log.warn("⚠ Список измерений пустой (возможно новый сенсор)");
                } else {
                    // Показать статистику
                    double min = glucoseData.stream()
                            .mapToDouble(GlucoseMeasurementDto::getValueInMmol)
                            .min()
                            .orElse(0.0);

                    double max = glucoseData.stream()
                            .mapToDouble(GlucoseMeasurementDto::getValueInMmol)
                            .max()
                            .orElse(0.0);

                    double avg = glucoseData.stream()
                            .mapToDouble(GlucoseMeasurementDto::getValueInMmol)
                            .average()
                            .orElse(0.0);

                    log.info("Статистика глюкозы:");
                    log.info("  ├─ Минимум: {} ммоль/л", String.format("%.1f", min));
                    log.info("  ├─ Максимум: {} ммоль/л", String.format("%.1f", max));
                    log.info("  └─ Среднее: {} ммоль/л", String.format("%.1f", avg));

                    // Показать последние 5 измерений
                    log.info("Последние 5 измерений:");

                    glucoseData.stream()
                            .limit(5)
                            .forEach(m -> log.info("  └─ {} | {} ммоль/л",
                                    m.getTimeStamp(),
                                    m.getValueInMmol()));

                }

            } catch (Exception e) {
                log.error("❌ ОШИБКА при получении данных глюкозы!", e);
            }

            log.info("========================================");

        };
    }
}
