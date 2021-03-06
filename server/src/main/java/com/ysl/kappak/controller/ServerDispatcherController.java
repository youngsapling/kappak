package com.ysl.kappak.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.google.common.base.Strings;
import com.ysl.kappak.config.WebSocketServer;
import com.ysl.kappak.config.kappakconfig.KappakConfigWrapper;
import com.ysl.kappak.entity.Bee;
import com.ysl.kappak.request.RequestBodyHttpServletRequestWrapper;
import com.ysl.kappak.util.HttpHelper;
import com.ysl.kappak.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/18 16:29
 * @modifyTime :
 * @description : 服务端伪分发器.
 */
@RestController
@RequestMapping("/**")
@Slf4j
public class ServerDispatcherController {
    @Autowired
    WebSocketServer webSocketServer;
    @Autowired
    MessageServer messageServer;
    @Autowired
    KappakConfigWrapper kappakConfigWrapper;

    @RequestMapping(produces = "application/json;charset=utf-8")
    public Object server() {
        // 获取实际调用方法
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        RequestBodyHttpServletRequestWrapper requestWrapper = (RequestBodyHttpServletRequestWrapper) request;
        String url = requestWrapper.getRequestURI();
        Long id = IdWorker.createId();
        // 构建请求的参数
        String jsonParam = null;
        String requestMethod = requestWrapper.getMethod();
        // get requestBody
        if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(requestWrapper.getContentType())) {
            // 参数格式
            Map<String, String[]> parameterMap = request.getParameterMap();
            jsonParam = JSONObject.toJSONString(parameterMap);
        } else if (MediaType.APPLICATION_JSON_VALUE.equals(requestWrapper.getContentType())) {
            // json格式
            try {
                jsonParam = HttpHelper.getRequestBodyString(requestWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return String.format("RequestHeader [Content-Type] only apply [%s, %s].",
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE);
        }

        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        List<Map<String, String>> headerList = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!StringUtils.isEmpty(headerName)) {
                Map<String, String> header = new HashMap<>(1);
                header.put(headerName, requestWrapper.getHeader(headerName));
                headerList.add(header);
            }
        }

        Bee highBee = Bee.builder().uri(url).id(id).httpMethod(requestMethod).requestHeaders(headerList)
                .jsonString(jsonParam).build();
        // 在请求头中标识要调用的后端名称.
        String clientName = requestWrapper.getHeader("ClientName");
        WebSocketServer targetWS = webSocketServer.getTarget(clientName);
        if (null == targetWS) {
            return String.format("目标后台{%s}没有连接到服务器.", clientName);
        }
        try {
            // 发送
            targetWS.sendMessage(JSONObject.toJSONString(highBee));
        } catch (IOException e) {
            log.error("发送给[{}]时异常, exception:{}", clientName, e.getMessage());
            return new Bee();
        }

        String lowBeeString = null;
        Callable<Boolean> getResult = () -> !Strings.isNullOrEmpty(messageServer.get(id));
        Retryer<Boolean> retryer = kappakConfigWrapper.getRetryerRegistry().getRetryer();
        try {
            retryer.call(getResult);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            e.printStackTrace();
        }
        lowBeeString = messageServer.getAndRemove(id);
        if (null == lowBeeString) {
            log.error("已等待40秒, 没有获取到结果.");
            lowBeeString = "已等待40秒, 没有获取到结果.";
        }
        return lowBeeString;
    }
}
