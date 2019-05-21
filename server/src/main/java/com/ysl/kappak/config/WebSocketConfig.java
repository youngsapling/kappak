package com.ysl.kappak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/20 22:19
 * @modifyTime :
 * @description : copy from https://blog.csdn.net/j903829182/article/details/78342941?tdsourcetag=s_pctim_aiomsg
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
