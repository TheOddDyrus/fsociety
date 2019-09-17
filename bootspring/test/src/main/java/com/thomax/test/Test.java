package com.thomax.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Test {

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @RequestMapping("/test")
    public Object test() {
        return 123;
    }

}
