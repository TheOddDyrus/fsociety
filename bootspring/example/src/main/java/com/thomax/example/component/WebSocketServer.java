package com.thomax.example.component;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static AtomicLong total = new AtomicLong(0L);
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;

    /**
     * 连接成功调用的方法
     * */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        onlineCount.incrementAndGet();
        sendMessage(buildMessage());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * */
    @OnMessage
    public void onMessage(String message) {
        total.addAndGet(Long.valueOf(message));
        for (WebSocketServer item : webSocketSet) {
            item.sendMessage(buildMessage());
        }
    }

    /**
     * 连接关闭后调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        onlineCount.decrementAndGet();
    }

    /**
     * 发生异常时调用的方法
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildMessage() {
        return "在线人数：" + onlineCount + "\n" + "总分：" + total;
    }

}
