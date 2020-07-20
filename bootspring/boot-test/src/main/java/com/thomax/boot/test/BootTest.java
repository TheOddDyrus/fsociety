package com.thomax.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BootTest {

    public static void main(String[] args) {
        SpringApplication.run(BootTest.class, args);
    }

    @RequestMapping("/test")
    public Object test() {
        return "This project runs successfully!";
    }

}
