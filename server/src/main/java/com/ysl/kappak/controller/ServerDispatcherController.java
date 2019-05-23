package com.ysl.kappak.controller;

import com.alibaba.fastjson.JSONObject;
import com.ysl.kappak.config.WebSocketServer;
import com.ysl.kappak.entity.Bee;
import com.ysl.kappak.request.RequestBodyHttpServletRequestWrapper;
import com.ysl.kappak.util.HttpHelper;
import com.ysl.kappak.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/**")
@Slf4j
public class ServerDispatcherController {
    @Autowired
    WebSocketServer webSocketServer;

    @RequestMapping()
    public Object server() {
        // 获取实际调用方法
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        RequestBodyHttpServletRequestWrapper wrapper = (RequestBodyHttpServletRequestWrapper) request;
        String url = wrapper.getRequestURI();
        Long id = IdWorker.createId();
        String jsonString = null;
        try {
            jsonString = HttpHelper.getBodyString(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bee build = Bee.builder().uri(url).id(id).jsonString(jsonString).build();
        // 怎么样能拿到全量的参数, 暂时把参数都放置在json中.
        // 在请求头中标识要调用的后端名称.
        String targetName = wrapper.getHeader("target");
        WebSocketServer targetWS = webSocketServer.getTarget(targetName);
        try {
            targetWS.sendMessage(JSONObject.toJSONString(build));
        } catch (IOException e) {
            log.error("发送给[{}]时异常, exception:{}", targetName, e.getMessage());
        }
        return null;
    }

}
