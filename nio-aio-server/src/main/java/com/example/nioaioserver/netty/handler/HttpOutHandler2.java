package com.example.nioaioserver.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-29 11:53
 **/
@Slf4j
public class HttpOutHandler2 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("http out handler 2");
            FullHttpResponse response=(FullHttpResponse) msg;
            response.content().writeBytes("out handlder2".getBytes());
            //交给下一个　out handler
            ctx.write(response,promise).addListener(ChannelFutureListener.CLOSE);

    }
}
