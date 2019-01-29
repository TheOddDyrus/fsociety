package com.thomax.ribbon.controller;

import com.thomax.ribbon.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("/test")
    public String test(HttpServletRequest request, String t) {
        return request.getHeader("gp") + " from ribbon|" + testService.exec(t);
    }

}
