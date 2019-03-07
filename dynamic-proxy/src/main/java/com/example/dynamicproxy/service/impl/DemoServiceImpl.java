package com.example.dynamicproxy.service.impl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-07 10:27
 **/
public class DemoServiceImpl implements DemoService{
    @Override
    public void sout() {
        System.out.println("demo_service");
    }
}
