package com.jabes.librebot.bot.command;

import com.jabes.librebot.bot.common.CommonInfo;

/**
 * Базовый интерфейс для всех команд бота
 */
public interface Command {

    UserCommand getCommand();

    /**
     * Выполняет команду
     *
     * @param commonInfo входящие данные от пользователя
     * @return текст ответа пользователю
     */
    void execute(CommonInfo commonInfo);

    /**
     * Возвращает описание команды для /help
     */
    String getDescription();


}
