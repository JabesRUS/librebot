package com.jabes.librebot.bot.command;

import com.jabes.librebot.bot.Utils.BotMessages;
import com.jabes.librebot.bot.common.CommonInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpCommand implements Command{

    private final List<Command> allCommands;
    private final TelegramClient telegramClient;

    @Override
    public UserCommand getCommand() {
        return UserCommand.HELP;
    }

    @Override
    public void execute(CommonInfo commonInfo) {

        StringBuilder message = new StringBuilder("\uD83D\uDCCB Доступные команды:\n\n");

        for (Command command : allCommands) {

            if(!command.getCommand().getCommandName().equals("/help")) { // Пропускаем саму help команду в списке

                message.append(String.format("%s - %s%n",
                        command.getCommand().getCommandName(),
                        command.getDescription()));
            }
        }

        sendMessage(message.toString(), commonInfo);

    }

    @Override
    public String getDescription() {
        return BotMessages.HELP;
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
