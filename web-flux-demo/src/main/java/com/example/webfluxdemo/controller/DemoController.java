package com.example.webfluxdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-02-20 16:58
 **/
@RestController
public class DemoController {
    @GetMapping("demo")
    public Flux<String> demoString(){
        StringBuilder result=new StringBuilder("test");
        int[] i=new int[]{1};
        new Thread(()->{
            while (i[0]++<10){
                try {
                    TimeUnit.SECONDS.sleep(1);
                    result.append(i[0]);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
        return Flux.interval(Duration.ofSeconds(2)).log().map(l->result.toString()+"\n");
    }
}
