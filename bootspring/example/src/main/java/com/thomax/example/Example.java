package com.thomax.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.thomax.example.dao")
public class Example {

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }

}
