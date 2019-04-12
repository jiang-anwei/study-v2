package com.example.nioaioserver.nettyFileServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.binding.StringFormatter;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-04-08 13:41
 **/
@Slf4j
public class FileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出错", cause);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + ";charset=utf-8");
        response.content().writeBytes(("错误" + cause.toString()).getBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String url = msg.uri();

        File file = new File(URLDecoder.decode(url, "utf-8"));
        if (file.exists() && file.isFile()) {

//            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            //        response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment;filename=" + new String(file.getName().getBytes(), "ISO8859-1"))
//                    .set(HttpHeaderNames.CONTENT_LENGTH, file.length())
//                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);
            //方法1
            //response.content().writeBytes(new FileInputStream(file),Integer.valueOf(file.length()+""));
            //方法2
            //response.content().writeBytes(FileChannel.open(Paths.get(file.getAbsolutePath())),0,(int)file.length());
            //ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

            //方法3
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            //不是完整的httpResponse 需要在最后加上　LastHttpContent.EMPTY_LAST_CONTENT
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);

            //如果不加　len 就要关闭流　.addListener(ChannelFutureListener.CLOSE)
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, randomAccessFile.length());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
            response.headers().add(HttpHeaderNames.CONTENT_DISPOSITION, new String(String.format("attachment; filename=\"%s\"", file.getName()).getBytes("utf-8"),"ISO-8859-1"));
            ctx.write(response);
//        ctx.write(new ChunkedFile(file)); or
            ctx.write(new ChunkedFile(randomAccessFile), ctx.newProgressivePromise()).addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                    if (total < 0) {
                        log.warn("file {} transfer progress: {}", file.getName(), progress);
                    } else {
                        log.debug("file {} transfer progress: {}/{}", file.getName(), progress, total);
                    }
                }

                @Override
                public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                    log.info("file {} transfer complete.", file.getName());
                    randomAccessFile.close();
                }
            });
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(ChannelFutureListener.CLOSE);
        } else if (file.exists() && file.isDirectory()) {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            StringBuilder stringBuilder = new StringBuilder();
            File[] files = file.listFiles();
            stringBuilder.append("<ol>");
            stringBuilder.append("<li><a href=\"").append(file.getParentFile()).append("\">返回上一级</a></li>");
            for (File son : files) {
                stringBuilder.append("<li><a href=\"").append(son.getAbsolutePath()).append("\">").append(son.getName()).append("</a></li>");
            }
            stringBuilder.append("</ol>");
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8")
                    .set(HttpHeaderNames.CONTENT_LENGTH, stringBuilder.toString().length());
            response.content().writeBytes(stringBuilder.toString().getBytes());
            ctx.writeAndFlush(response);
        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + ";charset=UTF-8");
            response.content().writeBytes(String.format("file %s is not exists", file.getAbsolutePath()).getBytes());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

    }

    public static void main(String[] args) throws Exception {

    }
}
