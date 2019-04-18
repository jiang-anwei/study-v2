package com.example.distributedtranscation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.example.distributedtranscation.db1mapper",sqlSessionFactoryRef = "SqlSessionFactory1")
public class DistributedTranscationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedTranscationApplication.class, args);
    }

}
