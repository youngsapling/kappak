package com.ysl.kappak.config;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
@ServerEndpoint(value = "/server", configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class WebSocketServer {
    /**
     * 线程安全的Integer
     */
    private static AtomicInteger onlineCount;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userName") String userName, Session session, EndpointConfig config) {
        if (!Strings.isNullOrEmpty(userName)) {
            this.session = session;
            WebSocketServer old = webSocketMap.put(userName, this);
            if (null != old) {
                try {
                    if(old.session.isOpen()){
                        old.session.close();
                    }
                } catch (IOException e) {
                    log.error("关闭旧的连接失败, {}", e.getMessage());
                }
            }
            addOnlineCount();
            log.info("{} 用户建立连接成功！", userName);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "userName") String userName) {
        if (!Strings.isNullOrEmpty(userName)) {
            webSocketMap.remove(userName);
        }
        subOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam(value = "userName") String userName, String message, Session session) {
        // 转发给指定的客户端后台
        WebSocketServer webSocketServer = webSocketMap.get(userName);
        try {
            webSocketServer.sendMessage(message);
        } catch (IOException e) {
            log.error("转发给后台[{}]失败, e:", userName, e.getStackTrace());
        }
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

    public WebSocketServer getTarget(String name){
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
