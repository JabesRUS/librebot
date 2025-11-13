package com.jabes.librebot.service;

import com.jabes.librebot.config.LibreViewConfig;
import com.jabes.librebot.exception.LibreViewAuthException;
import com.jabes.librebot.model.dto.libreview.LibreViewAuthTicket;
import com.jabes.librebot.model.dto.libreview.LibreViewLoginRequest;
import com.jabes.librebot.model.dto.libreview.LibreViewLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;

@Service
@Slf4j  // Lombok создаст поле: private static final Logger log
@RequiredArgsConstructor
public class LibreViewAuthService {

    @Qualifier("libreViewWebClient")
    private final WebClient webClient;
    private final LibreViewConfig config;

    // ============= КЕШИРОВАНИЕ ТОКЕНА =============

    /**
     * JWT токен, закешированный в памяти.
     * null = еще не авторизовывались
     */
    private String cachedToken;

    /**
     * Время истечения токена (Unix timestamp в секундах).
     * null = еще не авторизовывались
     */
    private Long tokenExpires;

    // ============= ПУБЛИЧНЫЙ API =============

    /**
     * Возвращает актуальный JWT токен для запросов к LibreView API.
     *
     * Логика работы:
     * 1. Если токена нет (первый запрос) → выполнить авторизацию
     * 2. Если токен протух → выполнить повторную авторизацию
     * 3. Если токен актуален → вернуть закешированный токен
     *
     * @return актуальный JWT токен
     * @throws LibreViewAuthException если авторизация не удалась
     */
    public String getValidToken() {

        // Проверить нужна ли (пере)авторизация
        if (isTokenExpired()) {
            log.debug("Токен отсутствует или протух, выполняется авторизация...");
            login();
        }

        return cachedToken;
    }

    // ============= ПРИВАТНЫЕ МЕТОДЫ =============

    /**
     * Выполняет авторизацию в LibreView API и сохраняет токен.
     *
     * Алгоритм:
     * 1. Получить email и password из конфигурации
     * 2. Создать LibreViewLoginRequest
     * 3. Отправить POST запрос через WebClient
     * 4. Обработать ответ LibreViewLoginResponse
     * 5. Извлечь и сохранить токен и expires
     *
     * @throws LibreViewAuthException если авторизация не удалась
     */
    private void login() {
        log.info("Выполняется авторизация в LibreView API...");

        try {
            // 1. Получить credentials из конфигурации
            String email = config.getCredentials().getEmail();
            String password = config.getCredentials().getPassword();

            // 2. Создать DTO объект запроса
            LibreViewLoginRequest request = new LibreViewLoginRequest(email, password);

            log.debug("Отправка запроса авторизации для email: {}", email);

            LibreViewLoginResponse response = webClient
                    .post()
                    .uri("/llu/auth/login")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(LibreViewLoginResponse.class)
                    .block();// Синхронное ожидание ответа

            // 4. Проверить что ответ не null
            if (response == null || response.getData() == null) {
                throw new LibreViewAuthException("Получен пустой ответ от LibreView API");
            }

            // 5. Проверить статус ответа (0 = успех)
            if (response.getStatus() != 0) {
                throw new LibreViewAuthException("Ошибка авторизации, status: " + response.getStatus());
            }

            // 6. Извлечь токен
            LibreViewAuthTicket authTicket = response.getData().getAuthTicket();
            if (authTicket == null || authTicket.getToken() == null) {
                throw new LibreViewAuthException("Токен отсутствует в ответе");
            }

            // 7. Сохранить токен и expires
            this.cachedToken = authTicket.getToken();
            this.tokenExpires = authTicket.getExpires();
            log.info("Авторизация успешна! Токен действителен до: {}",
                    java.time.Instant.ofEpochSecond(tokenExpires));

        } catch (WebClientResponseException e) {
            // Ошибки HTTP (4xx, 5xx)
            log.error("Ошибка HTTP при авторизации: {} - {}", e.getStatusCode(), e.getMessage());
            throw new LibreViewAuthException("Ошибка авторизации: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // Другие ошибки (timeout, network, parsing)
            log.error("Неожиданная ошибка при авторизации", e);
            throw new LibreViewAuthException("Не удалось выполнить авторизацию: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @param newToken
     */
    public void updateToken(LibreViewAuthTicket newToken) {

        if (newToken == null || newToken.getToken() == null || newToken.getExpires() == null) {
            throw new LibreViewAuthException("Отсутствует токен или его срок действия.");
        }

        this.cachedToken = newToken.getToken();
        this.tokenExpires = newToken.getExpires();

        log.debug("Токен обновлен, действителен до: {}",
                Instant.ofEpochSecond(newToken.getExpires()));
    }

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @return true если токен протух или отсутствует, false если актуален
     */
    private boolean isTokenExpired() {
        // Если токена нет или expires нет → считаем протухшим
        if (tokenExpires == null || cachedToken == null) {
            return true;
        }
        // Получить текущее время в секундах (Unix timestamp)
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        // Сравнить с expires (добавить небольшой буфер 60 секунд для безопасности)
        return currentTimeSeconds >= (tokenExpires - 60);
    }
}
