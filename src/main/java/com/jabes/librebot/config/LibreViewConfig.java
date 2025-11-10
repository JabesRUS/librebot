package com.jabes.librebot.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "libreview")
@Validated
@Setter @Getter
public class LibreViewConfig {

    private ApiConfig api;
    private CredentialsConfig credentials;


    @Setter @Getter
    public static class ApiConfig {
        @NotBlank
        private String baseUrl;
        @Min(1000)
        private int connectionTimeout;
        @Min(1000)
        private int readTimeout;
        private HeadersConfig headers;
    }

    @Setter @Getter
    public static class HeadersConfig {
        private String product;
        private String version;
        private String accept;
        private String contentType;
        private String xAppPlatform;
        private String xAppVersion;
        private String xAppName;
    }

    @Setter @Getter
    public static class CredentialsConfig {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }


}
