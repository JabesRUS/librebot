package com.jabes.librebot.bot.command;

import com.jabes.librebot.bot.Utils.BotMessages;
import com.jabes.librebot.bot.common.CommonInfo;
import com.jabes.librebot.exception.LibreViewApiException;
import com.jabes.librebot.model.dto.libreview.GlucoseMeasurementDto;
import com.jabes.librebot.model.dto.libreview.LibreViewConnection;
import com.jabes.librebot.service.LibreViewApiService;
import com.jabes.librebot.service.MessageSender;
import com.jabes.librebot.service.UserService;
import com.jabes.librebot.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;


@Service
@RequiredArgsConstructor
@Slf4j
public class GlucoseCommand implements Command{

    private final MessageSender messageSender;
    private final UserService userService;
    private final LibreViewApiService libreViewApiService;

    @Override
    public UserCommand getCommand() {
        return UserCommand.GLUCOSE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String message = "";

        if (!userService.userExists(chatId)) {
            log.info("Пользователь {} с chatId {} не зарегистрирован.", commonInfo.getFirstName(), commonInfo.getChatId());
            message = BotMessages.NOT_REGISTERED_USER_MESSAGE;

        } else {
            try {
                LibreViewConnection libreViewConnection = getConnection();
                message = createMessage(libreViewConnection);
            } catch (LibreViewApiException e) {
                log.error("Ошибка API LibreView для chatId {}: {}", chatId, e.getMessage(), e);
                message = BotMessages.GLUCOSE_API_ERROR;
            } catch (Exception e) {
                log.error("Ошибка для chatId {}", chatId, e);
                message = "Не удалось получить данные. Попробуйте позже.";
            }
        }

        messageSender.sendAsync(message, commonInfo, getCommand().getCommandName(), "HTML");

    }

    private String createMessage(LibreViewConnection connection) {
        GlucoseMeasurementDto glucoseMeasurement = connection.getGlucoseMeasurement();
        String firstName = connection.getFirstName();
        String lastName = connection.getLastName();
        LocalDateTime timeStamp = DateUtils.parseUsDate(glucoseMeasurement.getTimeStamp());
        Double valueInMmol = glucoseMeasurement.getValueInMmol();
        Integer trendArrow = glucoseMeasurement.getTrendArrow();

        // Создаём форматтер для времени без секунд
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder message = new StringBuilder();
        message.append("<b>\uD83D\uDCCA Текущее показание для ");
        message.append(firstName);
        message.append(" ");
        message.append(lastName);
        message.append(".</b>");
        message.append(System.lineSeparator());
        message.append("Текущее показание: <code>");
        message.append(valueInMmol);
        message.append(" mmol/L </code>");
        message.append(getGlucoseLevelIndicator(valueInMmol));
        message.append(" ");
        message.append(getTrendArrowFromNumber(trendArrow));
        message.append(System.lineSeparator());
        message.append("Время измерения: <code>");
        message.append(timeStamp.toLocalTime().format(timeFormatter));
        message.append("</code>");

        return message.toString();
    }

    /**
     * Определяет индикатор уровня глюкозы по значению
     *
     * @param valueInMmol значение глюкозы в ммоль/л
     * @return строка с эмодзи и описанием уровня
     */
    private String getGlucoseLevelIndicator(Double valueInMmol) {
        if (valueInMmol < 3.0) {
            return "🔴 (очень низкий)";
        } else if (valueInMmol < 4.0) {
            return "🟠 (низкий)";
        } else if (valueInMmol <= 7.8) {
            return "🟢 (норма)";
        } else if (valueInMmol < 14.0) {
            return "🟡 (высокий)";
        } else {
            return "🔴 (очень высокий)";
        }
    }

    /**
     * Преобразует числовое значение тренда в символ со описанием
     *
     * @param trendArrow числовое значение тренда из LibreView API (1-5)
     * @return строка с символом и описанием тренда
     */
    private String getTrendArrowFromNumber(Integer trendArrow) {

        return switch (trendArrow) {
            case 1 -> "⬇\uFE0F⬇\uFE0F";
            case 2 -> "⬇\uFE0F";
            case 3 -> "➡\uFE0F";
            case 4 ->  "⬆\uFE0F";
            case 5 -> "⬆\uFE0F⬆\uFE0F";
            default -> "? (нет данных)";
        };
    }

    private LibreViewConnection getConnection() {
        List<LibreViewConnection> connections = libreViewApiService.getConnections();

        if (connections.isEmpty()) {
            throw new IllegalStateException("Список подключений пустой.");
        }

        return connections.get(0);
    }

    @Override
    public String getDescription() {
        return BotMessages.GLUCOSE_MESSAGE;
    }
}
