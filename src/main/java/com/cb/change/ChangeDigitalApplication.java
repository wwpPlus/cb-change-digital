package com.cb.change;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cb.change.mapper")
public class ChangeDigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangeDigitalApplication.class, args);
    }
}
