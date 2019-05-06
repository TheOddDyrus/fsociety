package com.thomax.letsgo.advanced.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Java原生的NIO来创建一个Server
 * 用cmd：telnet localhost 1234可以返回一次消息以后断开连接
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.bind(new InetSocketAddress(1234)); //绑定服务到自定义端口1234
        Selector selector = Selector.open(); //打开selector处理channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT); //注册selector到ServerSocket，并指定这是连接操作
        final ByteBuffer msg = ByteBuffer.wrap("welcome to connect!!\n".getBytes());
        for (;;) {
            try {
                selector.select(); //等待新的事件到来，会阻塞
            } catch (IOException e) {
                e.printStackTrace();
                //handle exception
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys(); //从收到的所有事件中获取SelectionKey实例
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) { //检查该事件是一个新的连接准备好接受
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE, msg.duplicate()); //注册selector到SocketChannel，并指定这是写入操作
                        System.out.println("接收到的连接来自：" + client);
                    }
                    if (key.isWritable()) { //检查socket是否准备好写数据
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) { //写入数据。如果网络饱和，连接是可写的，那么这个循环将写入数据，直到该ByteBuffer缓冲区是空的
                                break;
                            }
                        }
                        client.close(); //返回一次信息以后就关闭
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException e2) {
                        //在关闭时忽略
                    }
                }
            }
        }

    }

}
