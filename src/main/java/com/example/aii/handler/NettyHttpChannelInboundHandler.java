package com.example.aii.handler;

import com.example.aii.util.BlockingMap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class NettyHttpChannelInboundHandler extends SimpleChannelInboundHandler<String> {

    public BlockingMap<String, String> response = new BlockingMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        ChannelFuture future = channelHandlerContext.channel().close();
        response.put(future.channel().id().asShortText(), s, 2, TimeUnit.SECONDS);
    }
}
