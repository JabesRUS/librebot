package com.jabes.librebot.service;

import com.jabes.librebot.bot.common.CommonInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Сервис для отправки сообщений через Telegram API
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    private final TelegramClient telegramClient;

    /**
     * Асинхронно отправляет текстовое сообщение без форматирования
     */
    public void sendAsync(String text, CommonInfo commonInfo, String commandName) {
        sendAsync(text, commonInfo, commandName, null);
    }

    /**
     * Асинхронно отправляет текстовое сообщение пользователю
     *
     * @param text текст сообщения
     * @param commonInfo информация о пользователе и чате
     * @param commandName название команды для логирования
     */
    public void sendAsync(String text, CommonInfo commonInfo, String commandName, String parseMode) {
        Long chatId = commonInfo.getChatId();
        Long userId = commonInfo.getUserId();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        if (parseMode != null) {
            message.setParseMode(parseMode);
        }

        try {
            telegramClient.executeAsync(message)
                    .thenAccept(sentMessage ->
                            log.info("Ответ отправлен пользователю {} в чат {}. Message ID: {}, из команды {}",
                                    userId, chatId, sentMessage.getMessageId(), commandName)
                    )
                    .exceptionally(throwable -> {
                        log.error("Ошибка отправки сообщения пользователю {}: {}",
                                userId, throwable.getMessage(), throwable);
                        return null;
                    });

        } catch (TelegramApiException e) {
            log.error("Ошибка при создании запроса на отправку сообщения", e);
        }
    }

}
