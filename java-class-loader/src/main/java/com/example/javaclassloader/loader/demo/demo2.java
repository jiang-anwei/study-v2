package com.example.javaclassloader.loader.demo;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-01-04 16:06
 **/
public class demo2 {
    public void sout() {
        System.out.println("demo2->"+Thread.currentThread().getContextClassLoader());
        System.out.println("demo2-qwerqwerqe");
    }
    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.boot.class.path"));
    }
    }

