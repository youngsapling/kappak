package kappak.config.websocket;

import kappak.controller.ClientDispatcherController;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
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
    @Value("${client.clientName}")
    String clientName;
    @Value("${client.path}")
    String path;
    @Autowired
    ClientDispatcherController clientDispatcherController;

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            String uri = path;
            Map<String, String> httpHeaders = new HashMap<>(2);
            httpHeaders.put("clientName", clientName);
            WebSocketClient webSocketClient = new KappakSocketClient(new URI(uri), new Draft_6455(), httpHeaders,
                    100, clientDispatcherController, clientName);
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
