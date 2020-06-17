package com.example.nioaioserver.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jianganwei
 * @program study-v2
 * @description 套接字客户端 可见　这种服务器的瓶颈在于io 的速度，会使高效的cpu阻塞等待　io 显然这是极不合理的
 * @date 2019-03-27 10:30
 **/
@Slf4j
public class SocketClient {
    private static void connect(SelectionKey selectionKey, Selector selector) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            //正在连接完成连接
            if (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();
            }
            socketChannel.write(ByteBuffer.wrap("hello world\n".getBytes()));
            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void read(SelectionKey selectionKey, Selector selector) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            socketChannel.read(byteBuffer);
            log.info("得到服务端结果：{}", new String(byteBuffer.array()));
            socketChannel.close();
            selector.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void sendRequest() {
        try {
            Selector selector = SelectorProvider.provider().openSelector();
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(8000));
            channel.register(selector, SelectionKey.OP_CONNECT);
            while (true) {
                if (!selector.isOpen()) {
                    return;
                }
                selector.select();
                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> iterator = set.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //处理了需要删除
                    iterator.remove();
                    if (selectionKey.isConnectable()) {
                        connect(selectionKey, selector);
                    } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                        read(selectionKey, selector);
                    }
                }

            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public static void main(String[] args) throws Exception {
        CompletableFuture[] futures = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            futures[i] = CompletableFuture.runAsync(SocketClient::sendRequest);
        }
        CompletableFuture.allOf(futures).join();
//        SocketClient.sendRequest();
    }
}
