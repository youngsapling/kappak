package kappak.config.websocket;

import com.alibaba.fastjson.JSON;
import kappak.controller.ClientDispatcherController;
import kappak.entity.Bee;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

/**
 * @author: youngsapling
 * @date: 2019-05-30
 * @modifyTime:
 * @description: 自定义WebSocketClient
 */
@Data
@Slf4j
public class KappakSocketClient extends WebSocketClient {
    String clientName;
    ClientDispatcherController clientDispatcherController;

    public KappakSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout,
                              ClientDispatcherController clientDispatcherController, String clientName) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
        this.clientDispatcherController = clientDispatcherController;
        this.clientName = clientName;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[{}] 连接成功", clientName);
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
                toSource = e.getMessage();
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
}
