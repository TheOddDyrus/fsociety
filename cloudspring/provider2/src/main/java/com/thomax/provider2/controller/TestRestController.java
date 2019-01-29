package com.thomax.provider2.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/test")
    public String test(String t) {
        return (StringUtils.isEmpty(t) ? "port" : t) + ":" + port;
    }

}
