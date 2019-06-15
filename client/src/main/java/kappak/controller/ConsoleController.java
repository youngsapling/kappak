package kappak.controller;

import kappak.websocket.KappakSocketClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: youngsapling
 * @date: 2019-05-31
 * @modifyTime:
 * @description: 控制是否连接到WebSocketServer.
 */
@Data
@RestController
@RequestMapping(value = "/console")
public class ConsoleController {
    @Autowired
    KappakSocketClientBuilder builder;



    @RequestMapping(value = "/open")
    public Boolean open(){
        builder.build();
        return true;
    }

    @RequestMapping(value = "/close")
    public Boolean close(){
        builder.close();
        return true;
    }
}
