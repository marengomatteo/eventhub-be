package com.eventhub.gateway.configuration;

import java.nio.charset.StandardCharsets;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtCookieFilter implements GlobalFilter, Ordered {

    private static final String LOGIN_PATH = "/authentication/signin";
    private static final String GOOGLE_LOGIN_PATH = "/authentication/google";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (shouldModifyResponse(exchange)) {
            return modifyResponse(exchange, chain);
        }

        return chain.filter(exchange);
    }

    private boolean shouldModifyResponse(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();
        return path.contains(LOGIN_PATH) || path.contains(GOOGLE_LOGIN_PATH);
    }

    private Mono<Void> modifyResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);

                        String responseBody = new String(content, StandardCharsets.UTF_8);
                        log.info("Original response body: {}", responseBody);

                        // Modifica la risposta
                        String modifiedBody = modifyResponseBody(responseBody, exchange);
                        log.info("Modified response body: {}", modifiedBody);

                        return dataBufferFactory.wrap(modifiedBody.getBytes(StandardCharsets.UTF_8));
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private String modifyResponseBody(String responseBody, ServerWebExchange serverWebExchange) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(responseBody);

            String accessCookie = json.get("accessToken").asText();
            String refreshCookie = json.get("refreshToken").asText();

            setCookiesInResponse(serverWebExchange.getResponse(), accessCookie, refreshCookie);

            ObjectNode modifiedJson = (ObjectNode) json;
            modifiedJson.remove("accessToken");
            modifiedJson.remove("refreshToken");

            return modifiedJson.toString();
        } catch (Exception e) {
            log.error("Error modifying response body", e);
            return responseBody;
        }
    }

    private void setCookiesInResponse(ServerHttpResponse response, String accessCookie, String refreshCookie) {

        ResponseCookie accessHttpCookie = ResponseCookie.from("accessToken", extractCookieValue(accessCookie))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .domain(".127.0.0.1.nip.io")
                .path("/")
                .build();

        ResponseCookie refreshHttpCookie = ResponseCookie.from("refreshToken", extractCookieValue(refreshCookie))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .domain(".127.0.0.1.nip.io")
                .path("/")
                .build();

        response.addCookie(accessHttpCookie);
        response.addCookie(refreshHttpCookie);
    }

    private String extractCookieValue(String cookieString) {
        return cookieString;
    }

    @Override
    public int getOrder() {
        return -2;
    }
}