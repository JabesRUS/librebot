package com.jabes.librebot.repository;

import com.jabes.librebot.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по Telegram Chat ID
     * @param chatId - уникальный ID чата из Telegram
     * @return пользователь или Optional.empty()
     */
    Optional<User> findByChatId(Long chatId);

    /**
     * Проверить существование пользователя по Chat ID
     * @param chatId - ID чата Telegram
     * @return true если пользователь существует
     */
    boolean existsByChatId(Long chatId);

    /**
     * Найти пользователя по email LibreView
     * @param email - email для входа в LibreView
     * @return пользователь или Optional.empty()
     */
    Optional<User> findByLibreMail(String email);

    /**
     * Получить пользователей с активным токеном (с пагинацией)
     * @param pageable - параметры пагинации
     * @return страница с пользователями
     */
    Page<User> findAllByLibreTokenIsNotNull(Pageable pageable);


}
