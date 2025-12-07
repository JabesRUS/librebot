package com.jabes.librebot.bot.command;

import com.jabes.librebot.bot.Utils.BotMessages;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class TelegramCommandMenu {
    private final TelegramClient telegramClient;

    @PostConstruct
    public void registerCommand() {
        try {
            List<BotCommand> commands = List.of(
                    new BotCommand("start", BotMessages.WELCOME),
                    new BotCommand("register", BotMessages.REGISTER),
                    new BotCommand("glucose", BotMessages.GLUCOSE_MESSAGE),
                    new BotCommand("help", BotMessages.HELP)
            );
            telegramClient.execute(new SetMyCommands(commands, BotCommandScopeDefault.builder().build(), null));
        } catch (Exception e) {
            throw new RuntimeException("Failed to register commands: " + e.getMessage(), e);
        }




    }

}
