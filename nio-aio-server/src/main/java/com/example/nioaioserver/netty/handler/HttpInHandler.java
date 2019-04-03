package com.example.nioaioserver.netty.handler;

import com.alibaba.fastjson.JSON;
import com.example.nioaioserver.netty.action.Action;
import com.example.nioaioserver.netty.action.AnnoteAction;
import com.example.nioaioserver.netty.action.DemoAction;
import com.example.nioaioserver.util.SpringUtils;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author jianganwei
 * @program study-v2
 * @description 处理网络事件
 * @date 2019-03-28 15:04
 **/

@Slf4j
public class HttpInHandler extends ChannelInboundHandlerAdapter {
//    @Autowired
//    Action action;
    private static Map<String, String> actionMap = Maps.newHashMap();

    static {

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(AnnoteAction.class));
        Set<BeanDefinition> set = provider.findCandidateComponents("com.example.nioaioserver.netty.action");
        actionMap = set.stream().collect(Collectors.toMap(x -> {
            try {
                Class calss = Class.forName(x.getBeanClassName());
                AnnoteAction annotation = (AnnoteAction) calss.getAnnotation(AnnoteAction.class);
                return annotation.value();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }, BeanDefinition::getBeanClassName));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("错误：",cause);
        FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.valueOf(201));
        response.content().writeBytes("请求出错".getBytes());
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("register");
//        super.channelRegistered(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest request = (FullHttpRequest) msg;
                boolean keepAlive = HttpUtil.isKeepAlive(request);
                ctx.fireUserEventTriggered("string");
                log.info("in handler1:");

//                action = SpringUtils.getBean(DemoAction.class);
//
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
//                response.content().writeBytes(JSON.toJSONBytes(action.doAction(request)));


            if (actionMap.containsKey(request.uri())) {
                try {
                    Action action = (Action) Class.forName(actionMap.get(request.uri())).newInstance();
                    response.content().writeBytes(JSON.toJSONBytes(action.doAction(request)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                response.content().writeBytes(("hello netty url" + request.uri()).getBytes());
            }

                if (keepAlive) {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                }
                //调用write 方法会直接交给　ChannelInboundHandlerAdapter　处理
//                ctx.write(response);
                //交给下一个　ChannelInboundHandlerAdapter　处理

                ctx.fireChannelRead(msg);
            } else {
                log.info("dsadfadsf");
            }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    private static String getBody(FullHttpRequest request) {
        return request.content().toString(CharsetUtil.UTF_8);

    }

    public static void main(String[] args) throws Exception {


    }
}
