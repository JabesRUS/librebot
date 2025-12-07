package com.jabes.librebot.bot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserCommand {

    START("/start"),
    REGISTER("/register"),
    GLUCOSE("/glucose"),
    HELP("/help");

    private final String commandName;

    public static UserCommand fromString(String command) {
        for (UserCommand userCommand: UserCommand.values()) {
            if (userCommand.commandName.equals(command)) {
                return userCommand;
            }
        }
        return null;
    }
}
