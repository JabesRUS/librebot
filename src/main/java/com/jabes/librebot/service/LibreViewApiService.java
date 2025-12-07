package com.jabes.librebot.service;

import com.jabes.librebot.exception.LibreViewApiException;
import com.jabes.librebot.model.dto.libreview.GlucoseMeasurementDto;
import com.jabes.librebot.model.dto.libreview.LibreViewAuthTicket;
import com.jabes.librebot.model.dto.libreview.LibreViewConnection;
import com.jabes.librebot.model.dto.libreview.LibreViewConnectionsResponse;
import com.jabes.librebot.model.dto.libreview.LibreViewGraphResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LibreViewApiService {

    private final LibreViewAuthService authService;
    @Qualifier("libreViewWebClient")
    private final WebClient webClient;

    /**
     * Получает исторические данные глюкозы для указанного пациента.
     *
     * @param patientId ID пациента из LibreView
     * @return список измерений глюкозы (может быть пустым)
     * @throws LibreViewApiException если запрос не удался
     */
    public List<GlucoseMeasurementDto> getGlucoseData(String patientId) {
        String url = "/llu/connections/%s/graph".formatted(patientId);
        String validToken = authService.getValidToken();

        log.debug("Запрос списка архивных показаний graphData...");
        try {

            LibreViewGraphResponse response = webClient
                    .get()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                    .retrieve()
                    .bodyToMono(LibreViewGraphResponse.class)
                    .block();

            if (response == null) {
                throw new LibreViewApiException("Получен пустой ответ от API, LibreViewGraphResponse is NULL.");
            }

            if (response.getStatus() != 0) {
                throw new LibreViewApiException("Ошибка API, status: " + response.getStatus());
            }

            if (response.getGraphData() == null) {
                throw new LibreViewApiException("Поле data отсутствует в ответе.");
            }

            List<GlucoseMeasurementDto> glucoseMeasurements = response.getGraphData().getGlucoseMeasurements();
            if (glucoseMeasurements == null || glucoseMeasurements.isEmpty()) {
                log.warn("Список graphData пустой для patientId: {}", patientId);
                return List.of();  // Вернуть пустой неизменяемый список
            }

            log.info("Получено показаний в graphData: {}", glucoseMeasurements.size());

            // Обновляем токен если он изменился
            checkAndSaveToken(response.getTicket(), validToken);

            return glucoseMeasurements;


        } catch (WebClientResponseException e) {
            // Ошибки HTTP (4xx, 5xx)
            log.error("Ошибка при получении данных глюкозы: {} - {}", e.getStatusCode(), e.getMessage());
            throw new LibreViewApiException("Ошибка при получении данных глюкозы: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // Другие ошибки (timeout, network, parsing)
            log.error("Неожиданная ошибка при получении данных глюкозы", e);
            throw new LibreViewApiException("Не удалось получить данные глюкозы: " + e.getMessage(), e);
        }


    }


    /**
     * Получает список всех подключений (устройств) пользователя из LibreView.
     *
     * @return список подключений LibreView (не может быть пустым)
     * @throws LibreViewApiException если запрос не удался или список подключений пустой
     */
    public List<LibreViewConnection> getConnections() {
        String validToken = authService.getValidToken();

        log.debug("Запрос списка подключений...");

        try {
            LibreViewConnectionsResponse response = webClient
                    .get()
                    .uri("/llu/connections")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                    .retrieve()
                    .bodyToMono(LibreViewConnectionsResponse.class)
                    .block();

            if (response == null) {
                throw new LibreViewApiException("Получен пустой ответ от API");
            }

            if (response.getStatus() != 0) {
                throw new LibreViewApiException("Ошибка API, status: " + response.getStatus());
            }

            List<LibreViewConnection> connections = response.getData();

            if (connections.isEmpty()) {
                throw new LibreViewApiException("Список подключений пустой.");
            }

            log.info("Получено подключений: {}", connections.size());

            /**
             * Обновляем токен если он изменился
             */
            checkAndSaveToken(response.getTicket(), validToken);

            return connections;

        } catch (WebClientResponseException e) {
            // Ошибки HTTP (4xx, 5xx)
            log.error("Ошибка при получении подключений: {} - {}", e.getStatusCode(), e.getMessage());
            throw new LibreViewApiException("Ошибка при получении подключений: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // Другие ошибки (timeout, network, parsing)
            log.error("Неожиданная ошибка при получении подключений", e);
            throw new LibreViewApiException("Не удалось получить список подключений: " + e.getMessage(), e);
        }

    }

    public String getPatientId() {
        return getConnections().get(0).getPatientId();
    }

    private void checkAndSaveToken(LibreViewAuthTicket ticket, String validToken) {
        if (ticket != null) {
            String newToken = ticket.getToken();

            if (newToken != null && !newToken.equals(validToken)) {

                authService.updateToken(ticket);
                log.debug("Токен обновлен из ответа connections");

            }
        }
    }


}
