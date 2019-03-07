package com.example.dynamicproxy.service.impl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author jianganwei
 * @program study-v2
 * @description CGlib 动态代理不需要接口
 * @date 2019-03-07 10:58
 **/
public class CGlibProxyFactory implements MethodInterceptor {
    private Object targetObj;

    public CGlibProxyFactory(Object targetObj) {
        this.targetObj = targetObj;
    }

    public Object createNewProxy() {
        Enhancer enhancer = new Enhancer();
        //设置目标代理类的父类为目标代理类
        enhancer.setSuperclass(this.targetObj.getClass());
        //设置回调对象本身
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("start");
        Object result = method.invoke(targetObj, objects);
        System.out.println("end");
        return result;
    }
}
