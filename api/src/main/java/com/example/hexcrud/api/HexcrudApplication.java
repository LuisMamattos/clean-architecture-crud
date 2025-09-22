package com.example.hexcrud.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.example.hexcrud",   exclude = {DataSourceAutoConfiguration.class})
public class HexcrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexcrudApplication.class, args);
    }
}