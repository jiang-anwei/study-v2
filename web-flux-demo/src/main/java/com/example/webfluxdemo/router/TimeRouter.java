package com.example.webfluxdemo.router;

import com.example.webfluxdemo.hander.TimeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;

/**
 * @author jianganwei
 * @program study-v2
 * @description router 相当于传统的　spring mvc 的　＠RequestMapping
 * @date 2019-03-14 18:34
 **/
@Configuration
@Component("timeTouter1111")
public class TimeRouter {
    @Autowired
    TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timeRouter() {
        return RouterFunctions.route(RequestPredicates.GET("/time*"),
                request -> timeHandler.getTime(request));
    }
}
