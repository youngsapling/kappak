package com.ysl.kappak.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 数据
     */
    String jsonString;
}
