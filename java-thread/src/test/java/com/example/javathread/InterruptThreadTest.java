package com.example.javathread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author jianganwei
 * @program study-v2
 * @description 打断测试
 * @date 2019-02-18 17:11
 **/
public class InterruptThreadTest {


    public static void main(String[] args) {

        //不能打断
        Thread t=new Thread(()->{
            while (true){
                System.out.println("12341");
            }
        });
        t.start();
        t.interrupt();
    }
    @Test
    public void test(){
        //能打断
        Thread t=new Thread(()->{
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("12341");
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("被打断");
                }
            }
        });
        t.start();
        t.interrupt();
    }
    @Test
    public void test1() throws Exception{
        //能打断
        Thread t=new Thread(){
            @Override
            public void run() {
                while (!this.isInterrupted()){
                    System.out.println(System.currentTimeMillis());
                }

            }
        };
        t.start();
        TimeUnit.SECONDS.sleep(2);
        t.interrupt();
        System.out.println("打断");
        TimeUnit.SECONDS.sleep(20000);
    }
    @Test
    public void test2() throws Exception{
        //能打断
        Thread t=new Thread(){
            @Override
            public void run() {
                while (!Thread.interrupted()){
                    System.out.println(System.currentTimeMillis());
                }

            }
        };
        t.start();
        TimeUnit.SECONDS.sleep(2);
        t.interrupt();
        TimeUnit.SECONDS.sleep(20000);
    }
}
