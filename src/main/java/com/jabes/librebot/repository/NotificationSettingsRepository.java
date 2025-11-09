package com.jabes.librebot.repository;

import com.jabes.librebot.model.entity.NotificationSettings;
import com.jabes.librebot.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

    /**
     * Найти настройки конкретного пользователя
     * @param user - пользователь
     * @return настройки или Optional.empty()
     */
    Optional<NotificationSettings> findByUser(User user);

    /**
     * Получить пользователей с включёнными уведомлениями (с пагинацией)
     * @param pageable - параметры пагинации
     * @return страница с настройками
     */
    Page<NotificationSettings> findAllByNotificationsEnabledIsTrue(Pageable pageable);

    /**
     * Удалить настройки пользователя
     * @param user - пользователь
     */
    void deleteByUser(User user);

}
