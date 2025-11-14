package com.jabes.librebot.bot;

import com.jabes.librebot.bot.common.CommonInfo;
import com.jabes.librebot.bot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
@Slf4j
@RequiredArgsConstructor
public class LibreBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    public static final String START = "start";
    public static final String HELP = "help";

    private final TelegramClient telegramClient;
    private final BotConfig botConfig;

    // Метод регистрации - возвращает токен
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    // Метод регистрации - возвращает обработчик (сам себя)
    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() { // сообщает Spring Boot Telegram Starter-у какой класс будет обрабатывать входящие Update
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            CommonInfo commonInfo = getCommonInfo(message);

            if (message.hasText()) {

                if (message.isCommand()) { // Возвращает true, если сообщение - команда
                    String commandName = message.getText().substring(1);

                    switch (commandName) {
                        case START -> sendMessage(BotMessages.WELCOME, commonInfo);

                        case HELP -> sendMessage(BotMessages.HELP, commonInfo);

                        default -> sendMessage(BotMessages.UNKNOWN_COMMAND, commonInfo);
                    }
                } else {
                    Long chatId = commonInfo.getChatId();
                    Long userId = commonInfo.getUserId();
                    String userName = commonInfo.getUserName();
                    String firstName = commonInfo.getFirstName();
                    String messageText = commonInfo.getMessageText();

                    log.info(BotMessages.LOG_MESSAGE, userId, userName, firstName, chatId, messageText);

                    String messageResponse = "Вы написали: " + messageText;
                    sendMessage(messageResponse, commonInfo);
                }

            } else {
                log.debug("Получено сообщение без текста.");
            }
        } else {
            log.debug("Получено обновление, не являющееся сообщением");
        }
    }

    private void sendMessage(String text, CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        Long userId = commonInfo.getUserId();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            telegramClient.executeAsync(message)
                    .thenAccept(sentMessage ->
                        // Этот код выполнится ПОСЛЕ успешной отправки
                        log.info("Ответ отправлен пользователю {} в чат {}. Message ID: {}",
                                userId, chatId, sentMessage.getMessageId())
                    )
                    .exceptionally(throwable -> {
                        log.error("Ошибка отправки сообщения пользователю {}: {}",
                                userId, throwable.getMessage(), throwable);
                        return null;
                    });

        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }

    private CommonInfo getCommonInfo(Message message) {
        return CommonInfo.builder()
                .chatId(message.getChatId())
                .userId(message.getFrom().getId())
                .userFromTelegram(message.getFrom())
                .userName(message.getFrom().getUserName() != null ?
                        message.getFrom().getUserName() : "без username")
                .firstName(message.getFrom().getFirstName())
                .messageText(message.getText())
                .build();

    }
}
