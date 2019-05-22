package kappak.config;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: youngsapling
 * @date: 2019-05-22
 * @modifyTime:
 * @description:
 */
@Slf4j
@Component
public class WebSocketConfig {
    /**
     * 配置文件中填写.
     */
    @Value("${client.name}")
    String myName;

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            String uri = "ws://127.0.0.1:9090/server/" + myName;
            Map<String, String> httpHeaders = new HashMap<>(2);
            httpHeaders.put("name", myName);
            WebSocketClient webSocketClient = new WebSocketClient(new URI(uri), new Draft_6455(), httpHeaders,
                    100) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("[webSocket] 连接成功");
                }

                @Override
                public void onMessage(String message) {
                    log.info("[webSocket] 收到消息={}", message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[webSocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[webSocket] 连接错误={}", ex.getMessage());
                }
            };
            webSocketClient.connect();
            AtomicInteger atomicInteger = new AtomicInteger();
            while(!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN) && atomicInteger.get() <= 10){
                System.out.println("还没有打开");
                atomicInteger.incrementAndGet();
            }
            System.out.println("打开了");
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
