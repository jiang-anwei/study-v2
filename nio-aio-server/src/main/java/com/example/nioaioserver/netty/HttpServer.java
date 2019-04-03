package com.example.nioaioserver.netty;

import com.example.nioaioserver.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import lombok.Cleanup;

/**
 * @author jianganwei
 * @program study-v2
 * @description http 服务器
 * @date 2019-03-28 14:30
 **/

public class HttpServer {
    public static void startServer(int port) {

//          创建两个线程组　用于处理网路事件
//          一个用来接收客户端的连接
//         一个用来进行SocketChannel 的网络读写
        @Cleanup(value = "shutdownGracefully") EventLoopGroup bossGroup = new NioEventLoopGroup();
        @Cleanup(value = "shutdownGracefully") EventLoopGroup workGroup = new NioEventLoopGroup();
        //辅助启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .channel(NioServerSocketChannel.class)
                //注册两个线程组
                .group(bossGroup, workGroup)
                //tcp 属性，不能处理的的请求放入队队列的队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                //tcp 属性　长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast("encoder", new HttpResponseEncoder());
                        // ChannelInboundHandlerAdapter 必须在 out之后，　in　执行顺序为 顺序，out 为倒叙
                        ch.pipeline().addLast(new HttpOutHandler());
                        ch.pipeline().addLast(new HttpOutHandler2());
                        ch.pipeline().addLast("decoder", new HttpRequestDecoder());
                        //http 请求片段整合得到完整的FullHttpRequest
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
                        ch.pipeline().addLast(new HttpInHandler());
                        ch.pipeline().addLast(new HttpInHandler2());
                        ch.pipeline().addLast(new HttpInHandler3());
                    }
                });

        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        startServer(8085);
//    }
}
