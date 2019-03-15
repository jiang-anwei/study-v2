package com.example.webfluxdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jianganwei
 * @program study-v2　Flux　Mono 发布订阅者模式
 * @description 方式１
 * @date 2019-02-20 16:58
 **/
@RestController
public class DemoController {
    @GetMapping("demo")
    public Flux<String> demoString() {
        StringBuilder result = new StringBuilder("test");
        int[] i = new int[]{1};
        new Thread(() -> {
            while (i[0]++ < 10) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    result.append(i[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return Flux.range(1, 10).log().map(l -> result.toString() + "\n");
    }

    public static void main(String[] args) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Flux.zip(
                Flux.fromArray("zip ad ad ".split("\\s+")),
                Flux.interval(Duration.ofMillis(100))
        ).subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);
        Flux.fromArray("zip as dad".split("\\s+")).delayElements(Duration.ofMillis(1000)).subscribe(System.out::println,
                null,
                countDownLatch::countDown);
        countDownLatch.await();
    }
}
