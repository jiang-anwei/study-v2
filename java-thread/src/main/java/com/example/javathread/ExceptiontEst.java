package com.example.javathread;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author jianganwei
 * @program study-v2
 * @description executorService 中的异常会被隐藏，只能在　　runnable 中catch； 使用　excute 不会　或则submit 之后　取值　
 * @date 2019-01-16 09:55
 **/
public class ExceptiontEst {


    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("12341");
        list.add("132");

        ExecutorService executorService = Executors.newCachedThreadPool();

//        List<Future<?>> list1=list.stream().map(x->
//              executorService.submit(()->
//                System.out.println(x.split(",")[1]))
//
//        ).collect(Collectors.toList());
//        list1.forEach(x->{
//            try {
//                System.out.println(x.get());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        });

        list.forEach(x->{
                executorService.execute(()->{
                        System.out.println(x.split(",")[1]);
                });

        });

        executorService.shutdown();
    }
}
