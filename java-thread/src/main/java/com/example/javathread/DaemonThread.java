package com.example.javathread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-12 17:02
 **/
public class DaemonThread  {


    public static void main(String[] args) {
        ReentrantLock lock=new ReentrantLock();

       Thread t= new Thread(()->{
           while (true) {
               System.out.println("!34");
           }

       });

//       t.setDaemon(true);
       t.setDaemon(false);
       t.start();

    }

}
