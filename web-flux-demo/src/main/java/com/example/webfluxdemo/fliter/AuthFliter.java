package com.example.webfluxdemo.fliter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-15 10:30
 **/
@Component
public class AuthFliter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse reponse = serverWebExchange.getResponse();
        System.out.println(request.getPath());
        if (request.getPath().toString().equals("/time")) {
            return webFilterChain.filter(serverWebExchange);
        } else {
            return reponse.writeWith(Mono.just(reponse.bufferFactory().wrap(("dont have interface " + request.getPath().toString()).getBytes())));
        }

    }
}
