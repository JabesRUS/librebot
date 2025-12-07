package com.jabes.librebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Конфигурация планировщика задач
 */
@Configuration
public class SchedulerConfig {

    /**
     * Создаёт кастомный планировщик с пулом потоков
     *
     * @return настроенный TaskScheduler
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // Размер пула потоков (сколько задач могут выполняться одновременно)
        scheduler.setPoolSize(5);

        // Префикс для имени потоков (удобно в логах)
        scheduler.setThreadNamePrefix("glucose-scheduler-");

        // Максимальное время ожидания завершения задач при остановке (в секундах).
        scheduler.setAwaitTerminationSeconds(15);

        // При остановке приложения дождаться завершения текущих задач.
        scheduler.setWaitForTasksToCompleteOnShutdown(true);

        // Инициализация планировщика
        scheduler.initialize();

        return scheduler;
    }
}
