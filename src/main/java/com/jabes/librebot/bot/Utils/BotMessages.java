package com.jabes.librebot.bot.Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BotMessages {

    public static final String WELCOME =
            "Добро пожаловать в Libre Bot! Я помогу мониторить уровень глюкозы.";

    public static final String START_COMMAND = """
            👋 Привет, %s! \n
            Я Libre_Bot - помогу отслеживать уровень глюкозы.\n
            📌 Для начала работы используй команду /register\n
            ❓ Помощь: /help
            """;

    public static final String HELP = """
            Доступные команды:
            /start - начать работу
            /register - зарегистрироваться в системе
            /glucose - получить текущее состояние глюкозы
            /help - показать справку
            """;

    public static final String UNKNOWN_COMMAND =
            "Неизвестная команда. Используйте /help";

    public static final String LOG_MESSAGE = "Получено сообщение от пользователя [userId={}, username={}, firstName={}] \n" +
            "      в чате [chatId={}]: {}";

    public static final String GLUCOSE_MESSAGE = "Получить текущее показание глюкозы.";
    public static final String REGISTER = "Зарегистрироваться в системе";

    public static final String USER_EXISTS_MESSAGE = """
            ✅ Ты уже зарегистрирован!\n
            Используй /help для списка доступных команд.
            """;

    public static final String REGISTERED_SUCCEED_MESSAGE = """
            🎉 Регистрация успешна!\n\n
            Теперь я буду отслеживать твои показатели глюкозы.\n
            Используй /help чтобы узнать, что я умею.
            """;

    public static final String NOT_REGISTERED_USER_MESSAGE = """
            Для получения показаний необходимо зарегистрироваться.
            Для регистрации используйте команду /register
            """;
    public static final String GLUCOSE_API_ERROR = "";
}
