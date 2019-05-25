package com.ysl.kappak.config;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.ysl.kappak.controller.MessageServer;
import com.ysl.kappak.entity.Bee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/20 22:25
 * @modifyTime :
 * @description : copy from https://blog.csdn.net/j903829182/article/details/78342941?tdsourcetag=s_pctim_aiomsg
 */
@ServerEndpoint(value = "/kappak/{clientName}", configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class WebSocketServer {
    /**
     * 线程安全的Integer
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    //concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    protected static MessageServer messageServer;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam(value = "clientName") String clientName, Session session, EndpointConfig config) {
        if (!Strings.isNullOrEmpty(clientName)) {
            this.session = session;
            HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            WebSocketServer old = webSocketMap.put(clientName, this);
            if (null != old) {
                try {
                    if (old.session.isOpen()) {
                        old.session.close();
                    }
                } catch (IOException e) {
                    log.error("关闭旧的连接失败, {}", e.getMessage());
                }
            }
            addOnlineCount();
            log.info("{} 用户建立连接成功！", clientName);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "clientName") String clientName) {
        if (!Strings.isNullOrEmpty(clientName)) {
            webSocketMap.remove(clientName);
        }
        subOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 获取到了client端执行的结果.
        Bee lowBee = JSON.parseObject(message, Bee.class);
        Long id = lowBee.getId();
        String jsonString = lowBee.getJsonString();
        messageServer.put(id, jsonString);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public WebSocketServer getTarget(String name) {
        return webSocketMap.get(name);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.decrementAndGet();
    }

}
