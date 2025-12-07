package com.jabes.librebot.bot.command;

import com.jabes.librebot.bot.Utils.BotMessages;
import com.jabes.librebot.bot.common.CommonInfo;
import com.jabes.librebot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Обработчик команды /register - регистрация пользователя в системе
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterCommand implements Command{

    private final UserService userService;
    private final TelegramClient telegramClient;


    @Override
    public UserCommand getCommand() {
        return UserCommand.REGISTER;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String message;

        if (userService.userExists(chatId)) { // Проверяем, не зарегистрирован ли уже
            log.info("Пользователь с id {} уже зарегистрирован!", chatId);
            message = BotMessages.USER_EXISTS_MESSAGE;

        } else {
            userService.registerUser(chatId);
            message = BotMessages.REGISTERED_SUCCEED_MESSAGE;
        }

        sendMessage(message, commonInfo);

    }

    @Override
    public String getDescription() {
        return BotMessages.REGISTER;
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
                            log.info("Ответ отправлен пользователю {} в чат {}. Message ID: {}, из команды {}",
                                    userId, chatId, sentMessage.getMessageId(), getCommand().getCommandName())
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
}
