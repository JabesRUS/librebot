package com.jabes.librebot.bot.command;

import com.jabes.librebot.bot.Utils.BotMessages;
import com.jabes.librebot.bot.common.CommonInfo;
import com.jabes.librebot.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartCommand implements Command{

    private final TelegramClient telegramClient;
    private final MessageSender messageSender;

    @Override
    public UserCommand getCommand() {
        return UserCommand.START;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        String firstName = commonInfo.getFirstName();
        String message = String.format(BotMessages.START_COMMAND, firstName);

        messageSender.sendAsync(message, commonInfo, getCommand().getCommandName());

    }

    @Override
    public String getDescription() {
        return BotMessages.WELCOME;
    }

}
