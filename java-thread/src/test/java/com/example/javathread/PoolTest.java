package com.example.javathread;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jianganwei
 * @program study-v2
 * @description 线程池测试
 * @date 2019-03-05 09:54
 **/
public class PoolTest {
    @Test
    public void demo() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> ii = executorService.submit(() -> {
            int[] i = new int[]{1};
            return i[2];
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                System.out.println(1234);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        });
//        ii.get();
//        TimeUnit.SECONDS.sleep(20);
        executorService.shutdown();
        System.out.println(executorService.awaitTermination(5, TimeUnit.SECONDS));

    }

    @Test
    public void demo1() throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();
        int[] i = new int[]{1};
       Future<int[]> ii= executorService.submit(() -> {
            i[0] = 2;
        }, null);
        System.out.println(ii.get());
        System.out.println(i[0]);
        executorService.shutdown();
        executorService.awaitTermination(10,TimeUnit.HOURS);
    }
    @Test
    public void demo2() throws Exception{
        FutureTask<String> task=new FutureTask<>(()->{
            TimeUnit.SECONDS.sleep(5);
            return "12312";
        });
        Thread t=new Thread(task);
        t.start();
        System.out.println(task.get());
    }
    @Test
    public void demo3()throws Exception{
//        CyclicBarrier cyclicBarrier=new CyclicBarrier();
        Lock lock=new ReentrantLock();
    }
}
