package com.thomax.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class TestController {

    @RequestMapping("/index")
    public String testHTML(HashMap<String, Object> map) {
        map.put("hello", "welcome to summoner's rift");
        return "index";
    }

    @RequestMapping("/websocket")
    public String testWebsocket() {
        return "websocket";
    }

}


