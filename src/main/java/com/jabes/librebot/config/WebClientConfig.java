package com.jabes.librebot.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient libreViewWebClient(LibreViewConfig config) {

        log.info("========================================");
        log.info("СОЗДАНИЕ LibreView WebClient");
        log.info("========================================");

        int connectionTimeout = config.getApi().getConnectionTimeout();
        int readTimeout = config.getApi().getReadTimeout();
        String baseUrl = config.getApi().getBaseUrl();
        LibreViewConfig.HeadersConfig headers = config.getApi().getHeaders();

        log.info("Base URL: {}", baseUrl);
        log.info("Connection timeout: {} ms", connectionTimeout);
        log.info("Read timeout: {} ms", readTimeout);
        log.info("Headers:");
        log.info("  ├─ product: {}", headers.getProduct());
        log.info("  ├─ version: {}", headers.getVersion());
        log.info("  ├─ accept: {}", headers.getAccept());
        log.info("  ├─ x-app-platform: {}", headers.getXAppPlatform());
        log.info("  ├─ x-app-version: {}", headers.getXAppVersion());
        log.info("  └─ x-app-name: {}", headers.getXAppName());

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                );

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .defaultHeader("product", headers.getProduct())
                .defaultHeader("version", headers.getVersion())
                .defaultHeader(HttpHeaders.ACCEPT, headers.getAccept())
                .defaultHeader("x-app-platform", headers.getXAppPlatform())
                .defaultHeader("x-app-version", headers.getXAppVersion())
                .defaultHeader("x-app-name", headers.getXAppName())
                .defaultHeader("account-Id", headers.getAccountId())
                .build();

        log.info("✅ LibreView WebClient создан успешно!");
        log.info("========================================");

        return webClient;

    }
}
