package com.thomax.letsgo.advanced.network;

import com.thomax.letsgo.utils.StringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 利用Netty实现一个响应服务
 * 在cmd: telnet localhost 1234连接以后，可以输入消息，会得到相应
 */
public class NettyTelnet {
    public static void main(String[] args) {
        NettyTelnetServer nettyTelnetServer = new NettyTelnetServer();
        try {
            nettyTelnetServer.open();
        } catch (InterruptedException e) {
            nettyTelnetServer.close();
        }
    }
}

class NettyTelnetServer {
    private static final int PORT = 1234;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void open() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .option(ChannelOption.SO_BACKLOG, 1024)
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new NettyTelnetInitializer());
        Channel channel = serverBootstrap
                .bind(PORT)
                .sync()
                .channel();
        channel.closeFuture().sync();
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

class NettyTelnetInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new NettyTelnetHandler());
    }
}

class NettyTelnetHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.write("Welcome to thomax's netty space!!\n");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String request) {
        String response;
        boolean close = false;
        if (StringUtil.isEmpty(request)) {
            response = "Please write some letter:\n";
        } else if ("bye".equals(request.toLowerCase())) {
            response = "Adios!!\n";
            close = true;
        } else {
            response = "response for '" + request + "'\n";
        }

        ChannelFuture future = channelHandlerContext.write(response);
        channelHandlerContext.flush();
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
