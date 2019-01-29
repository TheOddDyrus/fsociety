package com.thomax.gateway.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class TestConfig {

    /*@Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        String httpUri = "http://localhost:8764";
        return builder.routes()
                .route(p -> p
                        .path("/ri")
                        .filters(f -> f.addRequestHeader("gp", "gatewayParam"))
                        .uri(httpUri))
                .route(p -> p
                        .host("localhost")
                        .filters(f -> f
                                .hystrix(config -> config
                                        .setName("riFallback")
                                        .setFallbackUri("forward:/riFallback")))
                        .uri(httpUri))
                .build();
    }*/

}
