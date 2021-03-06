package kappak.websocket;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import kappak.config.eventbus.ConnectionCloseEvent;
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
    /**
     * 是否进行重连
     */
    boolean reTry;
    /**
     * 标识自己
     */
    String clientName;
    /**
     * 事件总线
     */
    EventBus eventBus;
    /**
     * 前端控制器
     */
    ClientDispatcherController clientDispatcherController;
    /**
     * 通过本地http请求调用本地真实请求, 那么client端需要独立运行于本地. type = 1
     * 通过方法反射调用, 那么client端和真实服务在一起. type = 2
     */
    Integer methodType;

    String serverPort;

    public KappakSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout,
                              ClientDispatcherController clientDispatcherController, String clientName, EventBus eventBus,
                              Integer methodType, String serverPort) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
        this.clientDispatcherController = clientDispatcherController;
        this.clientName = clientName;
        this.eventBus = eventBus;
        this.methodType = methodType;
        this.serverPort = serverPort;
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
                switch (this.methodType) {
                    case 1 :
                        toSource = clientDispatcherController.sendRest(highBee, serverPort);
                        break;
                    case 2 :
                        toSource = clientDispatcherController.reflect(highBee.getUri(), highBee.getJsonString());
                        break;
                    case 3 :
                        toSource = clientDispatcherController.dispatcher(highBee);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                toSource = e.getMessage();
            }
            // 消息回推.
            Bee lowBee = Bee.builder().id(id).jsonString(toSource).build();
            log.info("[client] 返回消息={}", JSON.toJSONString(lowBee));
            this.send(JSON.toJSONString(lowBee));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        eventBus.post(new ConnectionCloseEvent(reTry));
        log.info("[oldClient] 退出连接, [{}], [{}], [{}].", code, reason, reTry ? "已申请重新连接" : "没有重连");
    }

    @Override
    public void onError(Exception ex) {
        log.info("[client] 连接错误={}", ex.getMessage());
    }
}
