package com.example.dynamicproxy.service.impl;

import com.google.common.reflect.Reflection;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;
import java.util.ServiceLoader;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-07 10:05
 **/
@Service
public interface DemoService {
    void sout();


    //jdk 实现
//    public static void main(String[] args) {
//        DemoService demoService=(DemoService) Proxy.newProxyInstance(DemoService.class.getClassLoader(), new Class[]{DemoService.class}, (proxy, method, args1) -> {
//            System.out.println("start");
//            Object result=method.invoke(new DemoServiceImpl(),args1);
//            System.out.println("end");
//            return result;
//        });
//        demoService.sout();
//    }
//guava 实现

    public static void main(String[] args) {
//        DemoService demoService = Reflection.newProxy(DemoService.class, (proxy, method, aargs) -> {
//            System.out.println("start");
//
//            Object result=method.invoke(new DemoServiceImpl(),aargs);
//            System.out.println("end");
//            return result;
//        });
//        demoService.sout();

    }
//CGlib 动态代理不需要接口
//    public static void main(String[] args) {
//        CGlibProxyFactory cGlibProxyFactory = new CGlibProxyFactory(new DemoServiceImpl());
//        DemoServiceImpl demoService = (DemoServiceImpl) cGlibProxyFactory.createNewProxy();
//        demoService.sout();
//    }

}
