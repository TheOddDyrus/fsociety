package com.thomax.provider1.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestRestController {

    @Value("${server.port}")
    private String port;

    @Value("${test.admin}")
    private String testAdmin;

    @RequestMapping("/test")
    public String test(HttpServletRequest request, String t) {
        String gp = request.getHeader("gp");
        return gp + "|" + (StringUtils.isEmpty(testAdmin) ? "GitHub" : testAdmin) + "|" + (StringUtils.isEmpty(t) ? "port" : t) + ":" + port;
    }

}
