package com.example.javathread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author jianganwei
 * @program study-v2
 * @description executorService 中的异常会被隐藏，只能在runnable 中catch
 * @date 2019-01-16 09:55
 **/
public class test {


    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("12341");
        list.add("132");

        ExecutorService executorService = Executors.newCachedThreadPool();
        list.forEach(x->{
            try {
                executorService.submit(()->{
                    try {
                        System.out.println(x.split(",")[1]);
                    }
                   catch (Exception e){
                        e.printStackTrace();
                   }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        });
        executorService.shutdown();
    }
}
