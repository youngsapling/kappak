package com.ysl.kappak.controller;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/23 21:44
 * @modifyTime :
 * @description :
 */
@Component
public class MessageServer {
    /**
     * 消息map
     */
    private Map<Long, String> messageMap = new ConcurrentHashMap<>();

    public String put(Long id, String lowBeeString){
        return this.messageMap.put(id, lowBeeString);
    }

    public String get(Long id){
        String s = this.messageMap.get(id);
        this.messageMap.remove(id);
        return s;
    }
}
