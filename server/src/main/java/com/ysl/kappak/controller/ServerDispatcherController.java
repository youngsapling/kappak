package com.ysl.kappak.controller;

import com.alibaba.fastjson.JSONObject;
import com.ysl.kappak.config.WebSocketServer;
import com.ysl.kappak.entity.Bee;
import com.ysl.kappak.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/18 16:29
 * @modifyTime :
 * @description :
 */
@RestController
@RequestMapping("/44")
@Slf4j
public class ServerDispatcherController {
    @Autowired
    WebSocketServer webSocketServer;

    @RequestMapping()
    public Object server(String json) {
        // 获取实际调用方法
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        Long id = IdWorker.createId();
        JSONObject jsonObject = JSONObject.parseObject(json);
        Bee build = Bee.builder().uri(url).id(id).jsonObject(jsonObject).build();
        // 怎么样能拿到全量的参数, 暂时把参数都放置在json中.
        String targetName = request.getHeader("target");
        WebSocketServer targetWS = webSocketServer.getTarget(targetName);
        try {
            targetWS.sendMessage(JSONObject.toJSONString(build));
        } catch (IOException e) {
            log.error("发送给[{}]时异常, exception:{}", targetName, e.getMessage());
        }
        return null;
    }

}
