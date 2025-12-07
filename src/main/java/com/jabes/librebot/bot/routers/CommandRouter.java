package com.jabes.librebot.bot.routers;

import com.jabes.librebot.bot.command.Command;
import com.jabes.librebot.bot.command.UserCommand;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommandRouter {

    private final Map<UserCommand, Command> commandMap= new EnumMap<>(UserCommand.class);

    public CommandRouter(List<Command> commands) {
        commands.forEach(command -> commandMap.put(command.getCommand(), command));
    }

    public Optional<Command> getCommand(UserCommand userCommand) {
        return Optional.ofNullable(commandMap.get(userCommand));
    }

}
