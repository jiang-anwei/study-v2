package com.example.databasetransction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//@MapperScan(basePackages = "com.example.databasetransction.mapper")
public class DatabaseTransctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseTransctionApplication.class, args);
    }

}
