package com.jabes.librebot.repository;

import com.jabes.librebot.model.entity.GlucoseReading;
import com.jabes.librebot.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface GlucoseReadingRepository extends JpaRepository<GlucoseReading, Long> {

    /**
     * Найти показания пользователя (с пагинацией)
     * @param user - пользователь
     * @param pageable - параметры пагинации
     * @return страница с показаниями
     */
    Page<GlucoseReading> findByUser(User user, Pageable pageable);

    /**
     * Найти показания пользователя, отсортированные по дате (новые первыми)
     * @param user - пользователь
     * @param pageable - параметры пагинации
     * @return страница с показаниями, отсортированными по убыванию даты
     */
    Page<GlucoseReading> findByUserOrderByTimestampDesc(User user, Pageable pageable);


    /**
     * Получить последнее показание глюкозы для пользователя
     * @param user - пользователь
     * @return последнее показание или Optional.empty()
     */
    Optional<GlucoseReading> findFirstByUserOrderByTimestampDesc(User user);

    /**
     * Найти показания за определённый период времени
     * @param user - пользователь
     * @param start - начало периода
     * @param end - конец периода
     * @param pageable - параметры пагинации
     * @return список показаний за период
     */
    Page<GlucoseReading> findByUserAndTimestampBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
}
