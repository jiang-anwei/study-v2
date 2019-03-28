package com.example.nioaioserver.BIO;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
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
            socket.connect(new InetSocketAddress(8082));
//            @Cleanup OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info("发送请求");
            writer.println("hello");
//            writer.flush();
            LockSupport.parkNanos(1000*1000*1000);
            writer.println("world");
            writer.flush();
            String line;
//            while ((line = reader.readLine()) != null) {
//                log.info("服务器返回:{}", line);
//            }
            log.info(reader.readLine());
            log.info(reader.readLine());
            writer.close();
//            reader.close();
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception{
//        sendRequest();
        int[] i=new int[]{1,2};
        Arrays.stream(i).map(x->x+=1).forEach(System.out::println);


    }
}
