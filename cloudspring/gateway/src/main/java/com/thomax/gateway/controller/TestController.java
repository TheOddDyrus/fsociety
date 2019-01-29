package com.thomax.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("fallback")
public class TestController {

    @RequestMapping("/tp")
    public Mono<String> tp() {
        return Mono.just("tp -> fallback");
    }

    @RequestMapping("/ri")
    public Mono<String> ri() {
        return Mono.just("ri -> fallback");
    }

    @RequestMapping("/fe")
    public Mono<String> fe() {
        return Mono.just("fe -> fallback");
    }

}
