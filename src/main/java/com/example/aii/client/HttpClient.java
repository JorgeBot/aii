package com.example.aii.client;

import com.example.aii.autoconfigure.CustomProperties;
import com.example.aii.entity.HttpInterface;
import com.example.aii.handler.NettyHttpChannelInboundHandler;
import com.example.aii.util.LineFeedUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class HttpClient {

    @Resource
    private NettyHttpChannelInboundHandler inboundHandler;

    @Resource
    private Bootstrap bootstrap;

    @Resource
    private CustomProperties properties;

    public void run(HttpInterface in) {
        try {
            ChannelFuture future = bootstrap.connect(in.getHost(), in.getPort()).sync();
            future.channel().writeAndFlush(LineFeedUtils.toCRLF(in.getRequest()) + "\r\n");
            String channelId = future.channel().id().asShortText();
            in.setResponse(inboundHandler.response.get(channelId,
                    properties.getHttpClientTimeout(),
                    TimeUnit.MILLISECONDS)
            );
            inboundHandler.response.remove(channelId);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            in.setResponse("连接异常: " + e.getMessage());
        }
    }
}
