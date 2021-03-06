package com.example.aii;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AiiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiiApplication.class, args);
    }

}
