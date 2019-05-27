package com.ysl.kappak.config;

import com.ysl.kappak.controller.MessageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/20 22:19
 * @modifyTime :
 * @description :
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setMessageServer(MessageServer messageServer){
        WebSocketServer.messageServer = messageServer;
    }
}
