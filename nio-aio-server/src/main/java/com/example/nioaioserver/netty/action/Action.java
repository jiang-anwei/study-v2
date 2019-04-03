package com.example.nioaioserver.netty.action;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author jianganwei
 * @program study-v2
 * @description doAction
 * @date 2019-03-28 16:26
 **/
public interface Action {
    Object doAction(HttpRequest request);
}
