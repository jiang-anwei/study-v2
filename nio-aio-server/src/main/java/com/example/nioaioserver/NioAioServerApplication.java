package com.example.nioaioserver;

import com.example.nioaioserver.netty.HttpServer;
import com.example.nioaioserver.netty.action.DemoAction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DemoAction.class)
public class NioAioServerApplication {


    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(NioAioServerApplication.class, args);

    }

}
