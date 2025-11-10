package com.jabes.librebot.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient libreViewWebClient(LibreViewConfig config) {

        int connectionTimeout = config.getApi().getConnectionTimeout();
        int readTimeout = config.getApi().getReadTimeout();
        String baseUrl = config.getApi().getBaseUrl();
        LibreViewConfig.HeadersConfig headers = config.getApi().getHeaders();

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .defaultHeader("product", headers.getProduct())
                .defaultHeader("version", headers.getVersion())
                .defaultHeader(HttpHeaders.ACCEPT, headers.getAccept())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, headers.getContentType())
                .defaultHeader("x-app-platform", headers.getXAppPlatform())
                .defaultHeader("x-app-version", headers.getXAppVersion())
                .defaultHeader("x-app-name", headers.getXAppName())
                .build();
    }
}
