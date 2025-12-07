package com.jabes.librebot.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
@Setter @Getter
@EnableAsync
public class BotConfig {

    private String token;
    private String username;
    private String name;

/*    @Primary
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("vthread-", 0).factory());
    }*/

    @Bean
    public TelegramClient getTelegramClient() {
        return new OkHttpTelegramClient(token);
    }

}
