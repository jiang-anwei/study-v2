package com.example.nioaioserver.bio;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jianganwei
 * @program study-v2
 * @description 传统socket 服务器
 * @date 2019-03-27 10:14
 **/
@Slf4j
public class SocketServer {
    public static class HandleRequest implements Runnable {
        private Socket socket;

        public HandleRequest(Socket socket) {
            this.socket = socket;
        }

        @Override

        public void run() {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                log.info("服务器收到请求");
                String len;
                while ((len = reader.readLine()) != null) {
                    log.info("收到请求:{}", len);
                    writer.println(len);
                    writer.flush();

                }
                log.info("处理相应耗时：{}", stopwatch.stop());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new HandleRequest(socket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
