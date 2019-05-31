package kappak.config.websocket;

import com.google.common.eventbus.EventBus;
import kappak.controller.ClientDispatcherController;
import kappak.controller.ConsoleController;
import lombok.Data;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: youngsapling
 * @date: 2019-05-31
 * @modifyTime:
 * @description: KappakSocketClient 管理类
 */
@Data
@Component
public class KappakSocketClientBuilder {
    /**
     * 内部维护client.
     */
    private KappakSocketClient webSocketClient;
    /**
     * 配置文件中填写.
     */
    @Value("${client.clientName}")
    String clientName;
    @Value("${client.path}")
    String path;
    @Autowired
    ClientDispatcherController clientDispatcherController;
    @Autowired
    ConsoleController consoleController;
    @Autowired
    EventBus eventBus;

    public KappakSocketClient build(){
        if(null != webSocketClient && webSocketClient.isConnecting()){
            return webSocketClient;
        }
        String uri = path;
        Map<String, String> httpHeaders = new HashMap<>(2);
        httpHeaders.put("clientName", clientName);
        try {
             webSocketClient = new KappakSocketClient(new URI(uri), new Draft_6455(), httpHeaders,
                    100, clientDispatcherController, clientName, eventBus);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        webSocketClient.connect();
        return webSocketClient;
    }

    public void close() {
        if(webSocketClient.isClosing() || webSocketClient.isClosed()){
        }else {
            webSocketClient.setReTry(false);
            webSocketClient.close();
        }
    }
}
