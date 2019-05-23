package kappak.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kappak.controller.ClientDispatcherController;
import kappak.entity.Bee;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    ClientDispatcherController clientDispatcherController;

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
                    Bee bee = JSON.parseObject(message, Bee.class);
                    Long id = bee.getId();
                    String dispatcher;
                    try {
                        dispatcher = clientDispatcherController.dispatcher(bee.getUri(), bee.getJsonString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 消息回推回去.
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
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
