package com.example.nioaioserver.nio;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author jianganwei
 * @program study-v2
 * @description 传统socket 服务器
 * @date 2019-03-27 10:14
 **/
@Slf4j
public class SocketServer {

    /**
     * 建立连接
     *
     * @param selectionKey
     */
    private static void doAccept(SelectionKey selectionKey, Selector selector) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            //注册下一个感兴趣的状态为读
            SelectionKey clientKey = socketChannel.register(selector, SelectionKey.OP_READ);
            //绑定需要需要回复给客户端的数据队列
            clientKey.attach(new LinkedBlockingDeque<ByteBuffer>());
            selector.wakeup();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * 读取数据
     *
     * @param selectionKey
     */
    private static void doRead(SelectionKey selectionKey, Selector selector) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int len = socketChannel.read(byteBuffer);
            if (len < 0) {
//                log.warn("读取到非法数据断开连接");
                socketChannel.close();
                return;
            }
            byteBuffer.flip();
            LinkedBlockingDeque<ByteBuffer> byteBuffers = (LinkedBlockingDeque<ByteBuffer>) selectionKey.attachment();
            byteBuffers.offer(byteBuffer);
            //注册下一个感兴趣的状态为读或者写
            selectionKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
            //唤醒阻塞在 select() 方法的线程
            selector.wakeup();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * 写数据
     *
     * @param selectionKey
     */
    private static void doWrite(SelectionKey selectionKey, Selector selector) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            LinkedBlockingDeque<ByteBuffer> byteBuffers = (LinkedBlockingDeque<ByteBuffer>) selectionKey.attachment();
            ByteBuffer byteBuffer = byteBuffers.take();
            int len = socketChannel.write(byteBuffer);
            if (len < 0) {
//                log.warn("读取到非法数据断开连接");
                socketChannel.close();
                selectionKey.cancel();
                return;
            }

            //注册下一个感兴趣的状态为读或者写
            if (byteBuffers.size() == 0) {
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
            //唤醒阻塞在 select() 方法的线程
            selector.wakeup();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    public static void main(String[] args) throws Exception {
        Selector selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        Map<SelectionKey, Stopwatch> map = Maps.newHashMap();
        channel.bind(new InetSocketAddress(8000));
        //向selector注册感兴趣的状态
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 阻塞直到有数据准备好(状态处于注册时的状态)
            selector.select();
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //处理了需要删除
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    doAccept(selectionKey, selector);
                }
                if (selectionKey.isValid() && selectionKey.isReadable()) {
                    map.putIfAbsent(selectionKey, Stopwatch.createStarted());
                    doRead(selectionKey, selector);
                }
                if (selectionKey.isValid() && selectionKey.isWritable()) {
                    doWrite(selectionKey, selector);
                    log.info("处理耗时:{}", map.get(selectionKey).stop());
                }
            }
        }
    }
}
