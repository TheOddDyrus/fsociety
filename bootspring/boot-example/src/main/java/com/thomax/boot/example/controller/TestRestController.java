package com.thomax.boot.example.controller;

import com.thomax.boot.example.service.AA;
import com.thomax.boot.example.service.OperationService;
import com.thomax.boot.example.component.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestRestController {

    @Autowired
    private OperationService operationService;

    @Resource(name = "tt")
    private AA aa;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WebSocketServer webSocketServer;

    @RequestMapping("/mybatis")
    public Object testMybatis() {
        return operationService.getTotal();
    }

    @RequestMapping("/redis")
    public Object testRedis() {
        return redisTemplate.boundHashOps("protocol:5371").get("8436");
    }

    @RequestMapping("/bean")
    public Object testBean() {
        return aa.test();
    }

    @RequestMapping("/exception")
    public Object testException() {
        throw new ArithmeticException("custom exception by __thomax");
    }

    @RequestMapping("/receiveMsg")
    public void testReceiveMsg(String message) {
        webSocketServer.onMessage(message);
    }

}
