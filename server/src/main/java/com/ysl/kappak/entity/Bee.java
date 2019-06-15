package com.ysl.kappak.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/21 21:55
 * @modifyTime :
 * @description : 通用装载数据的对象.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bee {
    /**
     * 目标uri
     */
    String uri;
    /**
     * 消息的唯一标识
     */
    Long id;
    /**
     * 请求方式
     */
    String httpMethod;
    /**
     * 请求头
     */
    List<Map<String, String>> requestHeaders;
    /**
     * 数据
     */
    String jsonString;
}
