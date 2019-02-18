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
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(1000);
                    System.out.println(">>>>>>>>>>>>"+str);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();

    }
    public static void main(String[] args) {
        SpringApplication.run(ApolloDemoApplication.class, args);
    }

}

