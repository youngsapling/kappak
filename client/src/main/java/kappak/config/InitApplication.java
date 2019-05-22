package kappak.config;

import kappak.test.MsgWebSocketClient;
import kappak.test.WebClientEnum;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author: youngsapling
 * @date: 2019-05-22
 * @modifyTime:
 * @description:
 */
@Component
@Order(value = 1)
public class InitApplication implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws URISyntaxException {
        WebClientEnum.initClient(new MsgWebSocketClient("ws://localhost:9090/websocket-server/server/yangle"));
//        WebSocketContainer container = null;
//        try {
//            container = ContainerProvider.getWebSocketContainer();
//        } catch (Exception ex) {
//            System.out.println("error" + ex);
//        }
//        try {
//            String uri = "ws://192.168.60.159:9090/server/yangle";
//            URI r = URI.create(uri);
//            Session session = container.connectToServer(Client.class, r);
//            System.out.println(1111111);
//        } catch (DeploymentException | IOException e) {
//            e.printStackTrace();
//        }
    }
}
