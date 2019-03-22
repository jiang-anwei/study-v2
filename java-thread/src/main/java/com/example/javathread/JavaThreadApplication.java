package com.example.javathread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class JavaThreadApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaThreadApplication.class, args);
    }

}

