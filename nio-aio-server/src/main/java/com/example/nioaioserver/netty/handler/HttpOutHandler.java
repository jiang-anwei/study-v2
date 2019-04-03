package com.example.nioaioserver.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-29 11:17
 **/
@Slf4j
public class HttpOutHandler extends ChannelOutboundHandlerAdapter {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            log.info("out Handler msg type:{}" ,msg.getClass().toString());
            FullHttpResponse response=(FullHttpResponse)msg;
            response.content().writeBytes("out handler".getBytes());
            log.info("content:{}",response.content().toString(CharsetUtil.UTF_8));
            //交给下一个
            ctx.write(response,promise);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
