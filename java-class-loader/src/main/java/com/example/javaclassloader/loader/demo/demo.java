package com.example.javaclassloader.loader.demo;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-01-04 15:01
 **/
public class demo {
    public void sout(){
        System.out.println("demo->"+Thread.currentThread().getContextClassLoader());
        System.out.println("v6");
        new demo2().sout();
    }
}
