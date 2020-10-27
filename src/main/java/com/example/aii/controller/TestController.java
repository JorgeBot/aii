package com.example.aii.controller;

import com.example.aii.entity.User;
import com.example.aii.handler.NettyHttpChannelInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @Resource
    private NettyHttpChannelInboundHandler nettyHttpChannelInboundHandler;

    @Resource
    private Bootstrap bootstrap;

    @PostMapping("/test")

    public User getUser(@RequestBody User user) {
        return user;
    }

    @GetMapping("/get")
    public String getM() throws InterruptedException {
        return "@@AA@@";
    }

    @GetMapping("/1")
    public void get1() {
        try {
            ChannelFuture future = bootstrap.connect("158.12.12.12", 80).sync();
            future.channel().writeAndFlush("GET /get HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "Accept: application/json, text/plain, */*\r\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36\r\n" +
                    "Origin: http://localhost:8080\r\n" +
                    "Sec-Fetch-Site: same-site\r\n" +
                    "Sec-Fetch-Mode: cors\r\n" +
                    "Sec-Fetch-Dest: empty\r\n" +
                    "Referer: http://localhost:8080/\r\n" +
                    "Accept-Encoding: gzip, deflate, br\r\n" +
                    "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                    "Cookie: sidebarStatus=0; JSESSIONID=E3EA330FE6BD9873ED65EED16D132059\r\n");
            System.out.println(nettyHttpChannelInboundHandler.response.get(future.channel().id().asShortText(), 1, TimeUnit.SECONDS));
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
