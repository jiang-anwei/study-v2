package com.example.webfluxdemo.hander;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author jianganwei
 * @program study-v2
 * @description handler 相当于传统spring mvc 的　controller 用来处理请求
 * @date 2019-03-14 18:25
 **/
@Component
public class TimeHandler {
    public Mono<ServerResponse> getTime(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just(LocalDateTime.now().toString()), String.class);

    }
}
