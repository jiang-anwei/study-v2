package com.example.nioaioserver.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author jianganwei
 * @program study-v2
 * @description 套接字客户端 可见　这种服务器的瓶颈在于io 的速度，会使高效的cpu阻塞等待　io 显然这是极不合理的
 * @date 2019-03-27 10:30
 **/
@Slf4j
public class SocketClient {
    public static void sendRequest() {
        try {
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            //注册 回调
            // 这里必须加 localhost 不知道为啥
            channel.connect(new InetSocketAddress("localhost",8000), null, new CompletionHandler<Void, Object>() {
                // 连接成功回调
                @Override
                public void completed(Void result, Object attachment) {
                    //写完数据回调 从服务端读取数据
                    channel.write(ByteBuffer.wrap("hello world".getBytes()), null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
                            //读完数据回调
                            channel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    byteBuffer.flip();
                                    log.info("服务器返回数据:{}", new String(attachment.array()));
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {

                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {

                        }
                    });
                }

                // 连接失败回调
                @Override
                public void failed(Throwable exc, Object attachment) {
                    log.error("错误", exc);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        CompletableFuture[] futures = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            futures[i] = CompletableFuture.runAsync(SocketClient::sendRequest);
        }
        CompletableFuture.allOf(futures).join();
        //由于方法全是异步的，不停一下main方法会马上退出
        TimeUnit.SECONDS.sleep(20);

    }
}
