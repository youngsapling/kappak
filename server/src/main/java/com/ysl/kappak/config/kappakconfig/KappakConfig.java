package com.ysl.kappak.config.kappakconfig;

import com.ysl.kappak.config.kappakconfig.dto.RetryerRegistry;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 13:50
 * @description : see WebMvcConfigurer
 */
public interface KappakConfig {
    /**
     * 添加自定义重试器
     * @param retryerRegistry
     */
    void addReTryEr(RetryerRegistry retryerRegistry);
}
