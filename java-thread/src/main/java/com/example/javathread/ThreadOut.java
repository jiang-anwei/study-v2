package com.example.javathread;

import java.util.concurrent.*;

/**
 * @author jianganwei
 * @program study-v2
 * @description 有子线程的时候主线程不会退出 除非子线程为守护进程
 * @date 2019-03-22 11:37
 **/
public class ThreadOut {
    public static void main(String[] args) {
//        ExecutorService executor = Executors.newCachedThreadPool();
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread();
                t.setDaemon(true);
                return t;
            }
        };
        ExecutorService executor = new ThreadPoolExecutor(1, 4, 10, TimeUnit.HOURS, new LinkedBlockingDeque<>(10), factory);
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("i am in " + Thread.currentThread().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        System.out.println(Thread.currentThread().toString());
//        executor.shutdown();
//        return;
    }
}

