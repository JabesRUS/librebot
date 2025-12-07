package com.jabes.librebot.service;

import com.jabes.librebot.exception.LibreViewApiException;
import com.jabes.librebot.model.dto.libreview.GlucoseMeasurementDto;
import com.jabes.librebot.model.entity.GlucoseReading;
import com.jabes.librebot.model.entity.User;
import com.jabes.librebot.repository.GlucoseReadingRepository;
import com.jabes.librebot.repository.UserRepository;
import com.jabes.librebot.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GlucoseSchedulerService {

    private final LibreViewApiService apiService;
    private final GlucoseReadingRepository readingRepository;
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60000) // каждую минуту (LibreView обновляет данные ~раз в минуту)
    public void getAndSaveGlucoseReading() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.debug("Нет зарегистрированных пользователей, пропускаем опрос");
            return;
        }

        try {
            GlucoseMeasurementDto glucoseMeasurement = apiService.getConnections().get(0).getGlucoseMeasurement();

            for (User user : users) {
                saveGlucoseReadingForUser(user, glucoseMeasurement);
            }

        } catch (LibreViewApiException e) {
            log.error("Ошибка получения данных из LibreView API: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Неожиданная ошибка в планировщике", e);
        }
    }

    private void saveGlucoseReadingForUser(User user, GlucoseMeasurementDto glucoseMeasurement) {
        GlucoseReading glucoseReading = buildGlucoseReading(glucoseMeasurement, user);
        LocalDateTime timestamp = glucoseReading.getTimestamp();

        if (readingRepository.existsByUserAndTimestamp(user, timestamp)) {
            log.debug("Показание уже занесено в БД для user {} и timestamp: {}", user.getId(), timestamp);
            return;
        }

        readingRepository.save(glucoseReading);
        log.info("Показание сохранено для user {}: {} mmol/L в {}",
                user.getId(), glucoseReading.getGlucoseValue(), timestamp);
    }

    private GlucoseReading buildGlucoseReading(GlucoseMeasurementDto glucoseMeasurement, User user) {
        if (glucoseMeasurement == null) {
            throw new LibreViewApiException("Получен пустой ответ от API");
        }

        Double valueInMmol = glucoseMeasurement.getValueInMmol();
        LocalDateTime timeStamp = DateUtils.parseUsDate(glucoseMeasurement.getTimeStamp());
        Integer trendArrow = glucoseMeasurement.getTrendArrow();

        GlucoseReading glucoseReading = new GlucoseReading();
        glucoseReading.setUser(user);
        glucoseReading.setGlucoseValue(BigDecimal.valueOf(valueInMmol));
        glucoseReading.setTimestamp(timeStamp);
        glucoseReading.setTrend(trendArrow.toString());

        return glucoseReading;
    }
}
