package kappak.config;

import javax.websocket.*;

/**
 * @author: youngsapling
 * @date: 2019-05-22
 * @modifyTime:
 * @description:
 */
@ClientEndpoint()
public class Client {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client onMessage: 1111");
    }
    @OnMessage
    public void onMessage(String message) {
        System.out.println("Client onMessage: 222" + message);
    }
    @OnClose
    public void onClose() {}
}
