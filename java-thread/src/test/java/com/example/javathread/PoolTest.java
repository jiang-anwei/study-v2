package com.example.javathread;

import org.junit.Test;

import java.util.concurrent.*;

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
        ii.get();
//        TimeUnit.SECONDS.sleep(20);
        executorService.shutdown();
        System.out.println(executorService.awaitTermination(1000, TimeUnit.SECONDS));

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
}
