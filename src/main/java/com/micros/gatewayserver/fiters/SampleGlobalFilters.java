package com.micros.gatewayserver.fiters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
@Slf4j
public class SampleGlobalFilters implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Executing global filter PRE");

        exchange.getRequest().mutate().headers(h -> h.add("token", "1234"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Executing global filter POST");
            String token = exchange.getRequest().getHeaders().getFirst("token");
            if (token != null) {
                log.info("token: {}", token);
                exchange.getResponse().getHeaders().add("token", token);
            }

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());
            // exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
