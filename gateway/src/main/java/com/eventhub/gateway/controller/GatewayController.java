package com.eventhub.gateway.controller;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.gateway.config.RouteConfig;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GatewayController {

    private final RouteConfig routeConfig;

    public GatewayController(RouteConfig routeConfig) {
        this.routeConfig = routeConfig;
    }
    
    @GetMapping("/{service}/**")
    public ResponseEntity<?> proxy(HttpServletRequest request,
                                    @PathVariable String service,
                                    ProxyExchange<byte[]> proxy) throws Exception {
        String requestUri = request.getRequestURI(); 
        String contextPath = "/" + service;
        String forwardPath = requestUri.substring(contextPath.length()); 

        String baseUri = routeConfig.getServices().get(service);
        if (baseUri == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown service: " + routeConfig.getServices());
        }

        String fullTargetUri = baseUri + "/" + service + forwardPath;

        System.out.println("Proxying to: " + fullTargetUri);
        return proxy.uri(fullTargetUri).get();
    }
}
