package com.eventhub.gateway.configuration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    public AuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            HttpCookie accessTokenCookie = request.getCookies().getFirst("accessToken");

            if (accessTokenCookie == null || accessTokenCookie.getValue().isEmpty()) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "application/json");

                String body = "{\"error\":\"Authentication required\",\"message\":\"Access token cookie not found\"}";
                DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
                return response.writeWith(Mono.just(buffer));
            }

            // Cookie presente - continua
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}