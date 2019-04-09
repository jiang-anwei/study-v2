package com.example.nioaioserver.nettyFileServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Cleanup;

import java.nio.charset.Charset;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-08 11:55
 **/
public class FileServer {
    public static void main(String[] args) {
        @Cleanup(value = "shutdownGracefully") EventLoopGroup bossGroup = new NioEventLoopGroup();
        @Cleanup(value = "shutdownGracefully") EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new FileServerHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind(8096).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
