package com.example.nioaioserver.nio;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
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
public class ThreadPoolSocketServer {

    private static int readWriteProccessCount = 5;
    private static int totalCount = 0;
    private static ReadWriteProccess[] proccesses = new ReadWriteProccess[readWriteProccessCount];

    static {
        for (int i = 0; i < readWriteProccessCount; i++) {
            proccesses[i] = new ReadWriteProccess();
        }
    }

    public static class ReadWriteProccess {
        private volatile boolean isRun = false;
        private Selector selector;

        public ReadWriteProccess() {
            try {
                selector = Selector.open();
//                run();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public void register(SocketChannel socketChannel) {
            try {
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(new LinkedBlockingDeque<ByteBuffer>());
                System.out.println("attach" + System.currentTimeMillis());
                run();
            } catch (ClosedChannelException e) {
                throw new IllegalStateException(e);
            }
        }

        public void wakeup() {
            selector.wakeup();
        }

        public void run() {
            if (!isRun) {
                isRun = true;
                new Thread(() -> {
                    while (true) {
                        // 阻塞直到有数据准备好(状态处于注册时的状态)
                        try {

                            //由于select方法与register 方法都需要获取相同的监视器，使用select会一直阻塞在register方法上所以采用 selectNow
//                            selector.select();
                            if (selector.selectNow() <= 0) {
                                continue;
                            }
                            Set<SelectionKey> set = selector.selectedKeys();
                            Iterator<SelectionKey> iterator = set.iterator();
                            while (iterator.hasNext()) {
                                SelectionKey selectionKey = iterator.next();
                                //处理了需要删除
                                iterator.remove();
                                if (selectionKey.isValid() && selectionKey.isReadable()) {
                                    doRead(selectionKey, selector);
                                }
                                if (selectionKey.isValid() && selectionKey.isWritable()) {
                                    doWrite(selectionKey, selector);
                                }
                            }
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }).start();
            }
        }

    }

    /**
     * 建立连接
     *
     * @param selectionKey
     */
    private static void doAccept(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            ReadWriteProccess proccess = proccesses[totalCount++%readWriteProccessCount];
            proccess.register(socketChannel);
            proccess.wakeup();

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
                log.warn("读取到非法数据断开连接");
                socketChannel.close();
                selectionKey.cancel();
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
                log.warn("--读取到非法数据断开连接");
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
                    doAccept(selectionKey);
                }
            }
        }
    }
}
