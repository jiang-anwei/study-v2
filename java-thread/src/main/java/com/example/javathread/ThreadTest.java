package com.example.javathread;

/**
 * @author jianganwei
 * @program study-v2
 * @description  sleep 不会释放锁　wait会释放锁
 * @date 2019-01-15 11:49
 **/
public class ThreadTest implements Runnable {
    int number = 10;

    public void firstMethod() throws Exception {
        synchronized (this) {
            number += 100;
            System.out.println(number);
        }
    }

    public void secondMethod() throws Exception {
        synchronized (this) {
            /**
             * (休息2S,阻塞线程)
             * 以验证当前线程对象的机锁被占用时,
             * 是否被可以访问其他同步代码块
             */
//            Thread.sleep(2000);
            this.wait(2000);
            number *= 200;
        }
    }

    @Override
    public void run() {
        try {
            firstMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadTest threadTest = new ThreadTest();
        Thread thread = new Thread(threadTest);
        thread.start();
        threadTest.secondMethod();
        System.out.println(threadTest.number);
    }
}
