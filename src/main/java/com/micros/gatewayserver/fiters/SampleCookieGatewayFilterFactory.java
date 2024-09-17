package com.micros.gatewayserver.fiters;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        log.info("Config: {}", config);
        return ((exchange, chain) -> {
            log.info("Executing pre filter: {}", config.getMessage());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.of(config.getMessage()).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.getCookieName(), config.getValue()).build());
                });
                log.info("Executing post filter: {}", config.getMessage());
            }));
        });
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigurationCookie {
        private String cookieName;
        private String value;
        private String message;
    }
}
