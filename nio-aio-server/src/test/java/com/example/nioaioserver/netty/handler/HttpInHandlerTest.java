package com.example.nioaioserver.netty.handler;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpInHandlerTest {
    @Test
    public void test() {
        EmbeddedChannel channel = new EmbeddedChannel(new HttpInHandler());

        //写一个入站消息
        System.out.println(channel.writeInbound(new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,"/test")));

    }


}