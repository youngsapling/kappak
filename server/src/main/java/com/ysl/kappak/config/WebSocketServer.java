package com.ysl.kappak.config;

import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/20 22:25
 * @modifyTime :
 * @description : copy from https://blog.csdn.net/j903829182/article/details/78342941?tdsourcetag=s_pctim_aiomsg
 */
@ServerEndpoint(value = "/websocket",configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServer {
    /**
     * 线程安全的Integer
     */
    private static AtomicInteger onlineCount;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        System.out.println("name is : " + httpSession.getAttribute("name"));
        //sessionMap.put(session.getId(), session);
        System.out.println("the session is : " + session.getId());
        System.out.println("session content is : " + session.getId());
        System.out.println("httpSession  is : " + httpSession.getId());
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            //sendMessage(CommonConstant.CURRENT_WANGING_NUMBER.toString());
            sendMessage("22222222");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        System.out.println("onMessage sessionId is : " + session.getId());
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        this.session.getBasicRemote().sendText(message + " : sendMessage id is : " + this.session.getId());
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
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
