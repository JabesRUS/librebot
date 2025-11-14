package com.jabes.librebot.bot.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.User;

@Setter @Getter
@Builder
public class CommonInfo {
    private Long chatId;
    private Long userId;
    private User userFromTelegram;
    private String userName;
    private String firstName;
    private String messageText;
}
