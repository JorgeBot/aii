package com.example.aii.config;

import com.example.aii.autoconfigure.CustomProperties;
import com.example.aii.handler.NettyHttpChannelInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class NettyConfig {

    @Resource
    CustomProperties properties;

    @Bean("httpBootstrap")
    public Bootstrap injectBootstrap(NettyHttpChannelInboundHandler nettyHttpChannelInboundHandler) {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup(1);
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getHttpClientTimeout())
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024*1024))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        p.addLast(new LineEncoder());
                        p.addLast(nettyHttpChannelInboundHandler);
                    }
                });
        return b;
    }
}
