package com.jabes.librebot.bot;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BotMessages {

    public static final String WELCOME =
            "Добро пожаловать в Libre Bot! Я помогу мониторить уровень глюкозы.";

    public static final String HELP = """
        Доступные команды:
        /start - начать работу
        /help - показать справку
        """;

    public static final String UNKNOWN_COMMAND =
            "Неизвестная команда. Используйте /help";

    public static final String LOG_MESSAGE = "Получено сообщение от пользователя [userId={}, username={}, firstName={}] \n" +
            "      в чате [chatId={}]: {}";
}
