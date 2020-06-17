package com.example.nioaioserver.aio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

/**
 * @author jianganwei
 * @program study-v2
 * @description 传统socket 服务器
 * @date 2019-03-27 10:14
 **/
@Slf4j
public class SocketServer {
    public static void start() throws Exception{
        AsynchronousServerSocketChannel socketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8000));
        //注册 回调
        socketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

            // 连接成功回调
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                Future<Integer> writeResult = null;
                try {
                    byteBuffer.clear();
                    //这里读写都是异步的
                    result.read(byteBuffer).get(100, TimeUnit.SECONDS);
                    byteBuffer.flip();
                    writeResult = result.write(byteBuffer);
                } catch (Exception e) {
                    log.error("错误", e);
                } finally {
                    try {
                        //服务器进行下一个客户端连接的准备
                        socketChannel.accept(null, this);
                        //等待数据写完
                        writeResult.get();
                        result.close();
                    } catch (Exception e) {
                        log.error("错误", e);
                    }
                }
            }

            //连接失败回调
            @Override
            public void failed(Throwable exc, Object attachment) {
                log.error("错误", exc);
            }
        });


    }

    public static void main(String[] args) throws Exception {
        start();
        //主线程可以继续操作
        TimeUnit.SECONDS.sleep(200);
    }
}
