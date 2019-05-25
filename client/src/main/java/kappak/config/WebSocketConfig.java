package kappak.config;

import com.alibaba.fastjson.JSON;
import kappak.controller.ClientDispatcherController;
import kappak.entity.Bee;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
    @Value("${client.path}")
    String path;
    @Autowired
    ClientDispatcherController clientDispatcherController;

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            String uri = path;
            Map<String, String> httpHeaders = new HashMap<>(2);
            httpHeaders.put("clientName", myName);
            WebSocketClient webSocketClient = new WebSocketClient(new URI(uri), new Draft_6455(), httpHeaders,
                    100) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("[{}] 连接成功", myName);
                }

                @Override
                public void onMessage(String message) {
                    try {
                        log.info("[client] 收到消息={}", message);
                        Bee highBee = JSON.parseObject(message, Bee.class);
                        Long id = highBee.getId();
                        String toSource = null;
                        try {
                            toSource = clientDispatcherController.dispatcher(highBee.getUri(), highBee.getJsonString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 消息回推.
                        Bee lowBee = Bee.builder().id(id).jsonString(toSource).build();
                        log.info("[client] 返回消息={}", JSON.toJSONString(lowBee));
                        this.send(JSON.toJSONString(lowBee));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[client] 退出连接, [], []", code, reason);
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[client] 连接错误={}", ex.getMessage());
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
