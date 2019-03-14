package com.example.javathread;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-12 16:50
 **/
public class DaemonThreadTest {
    @Test
    public void test() {
        Thread t = new Thread(() -> {
            while (true) {
                String s = "21341";
                try (OutputStream out = new FileOutputStream(new File("/home/jianganwei/logs/demo/demo.txt"), true)) {
                    out.write(s.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
//        t.setDaemon(true);
        t.start();
    }
}
