package com.eventhub.gateway.controller;

import java.util.Collections;
import java.util.Enumeration;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.gateway.config.RouteConfig;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GatewayController {

    private final RouteConfig routeConfig;

    @RequestMapping(value = "/api/{service}/**")
    public ResponseEntity<?> proxy(
            HttpServletRequest request,
            @Parameter(description = "Target microservice name") @PathVariable String service,
            @RequestBody(required = false) String body,
            ProxyExchange<byte[]> proxy) throws Exception {

        String requestUri = request.getRequestURI();
        String contextPath = "/api/" + service;
        String forwardPath = requestUri.substring(contextPath.length());

        if (forwardPath.isEmpty() || !forwardPath.startsWith("/")) {
            forwardPath = "/" + forwardPath;
        }

        String baseUri = routeConfig.getServices().get(service);
        if (baseUri == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Service not found: " + service + ". Available services: "
                            + routeConfig.getServices().keySet());
        }

        String fullTargetUri = baseUri + forwardPath;
        String queryString = request.getQueryString();
        if (queryString != null) {
            fullTargetUri += "?" + queryString;
        }

        System.out.println("Proxying " + request.getMethod() + " to: " + fullTargetUri);
        System.out.println("Body: " + body);

        // Copia gli headers
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // Evita headers che potrebbero causare problemi
            if (!headerName.toLowerCase().equals("host") &&
                    !headerName.toLowerCase().equals("content-length")) {
                headers.put(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        if (body != null && !headers.containsKey("Content-Type")) {
            headers.set("Content-Type", "application/json");
        }

        ProxyExchange<byte[]> configuredProxy = proxy.uri(fullTargetUri).headers(headers);

        return switch (request.getMethod().toUpperCase()) {
            case "GET" -> configuredProxy.get();
            case "POST" -> configuredProxy.body(body).post();
            case "PUT" -> configuredProxy.body(body).put();
            case "OPTIONS" -> configuredProxy.options();
            case "DELETE" -> configuredProxy.delete();
            case "PATCH" -> configuredProxy.body(body).patch();
            default -> ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        };
    }

    @GetMapping("/gateway/services")
    public ResponseEntity<?> getServices() {
        return ResponseEntity.ok(routeConfig.getServices());
    }
}
