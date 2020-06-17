package com.example.nioaioserver.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
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
    public static void sendRequest() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(8000));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
                log.info("发送请求");
                writer.print("hello");
                LockSupport.parkNanos(1000 * 1000 * 6000L);
                writer.println("world");
                writer.flush();
                log.info("服务器返回:{}", reader.readLine());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        CompletableFuture[] futures=new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            futures[i]=CompletableFuture.runAsync(SocketClient::sendRequest);
        }
        CompletableFuture.allOf(futures).join();

    }
}
