package com.example.apollodemo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableApolloConfig
public class ApolloDemoApplication {
    @Value("${demo.str:222}")
    private String str;
    @PostConstruct
    private void demo(){
        System.out.println(">>>>>>>>>>>>"+str);
    }
    public static void main(String[] args) {
        SpringApplication.run(ApolloDemoApplication.class, args);
    }

}

