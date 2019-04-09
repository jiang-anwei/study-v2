package com.example.nioaioserver.nettyFileServer;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import lombok.extern.slf4j.Slf4j;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        response.content().writeBytes(("错误" + cause.toString()).getBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String url = msg.uri();
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        File file = new File(URLDecoder.decode(url, "utf-8"));


//        response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment;filename=" + new String(file.getName().getBytes(), "ISO8859-1"))
//                    .set(HttpHeaderNames.CONTENT_LENGTH, file.length())
//                    .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM);
        //方法1
        //response.content().writeBytes(new FileInputStream(file),Integer.valueOf(file.length()+""));
        //方法2
        //response.content().writeBytes(FileChannel.open(Paths.get(file.getAbsolutePath())),0,(int)file.length());
        //ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");


        //如果不加　len 就要关闭流　.addListener(ChannelFutureListener.CLOSE)
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, randomAccessFile.length());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
        response.headers().add(HttpHeaderNames.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getName()));
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


    }

    public static void main(String[] args) throws Exception {
        String url = "/home/jianganwei/%E4%B8%8B%E8%BD%BD/e5a3857acdf44edd9446bf2adb93cf72_4.xlsx";
        File file = new File(URLDecoder.decode(url, "utf-8"));
        System.out.println(file.getAbsolutePath());
    }
}
