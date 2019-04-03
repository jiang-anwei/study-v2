package com.example.nioaioserver.netty.action;

import com.example.nioaioserver.netty.HttpServer;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-28 16:26
 **/
//@AnnoteAction("/demo")
//@Service
    @Slf4j
public class DemoAction implements Action{

    @Override
    public Object doAction(HttpRequest request) {
        return request.uri()+System.currentTimeMillis();
    }
    @PostConstruct
    private void demo(){
        new Thread(()->{
        log.info("start netty");
            HttpServer.startServer(8085);
            log.info("end netty");
        }).start();

    }
}
