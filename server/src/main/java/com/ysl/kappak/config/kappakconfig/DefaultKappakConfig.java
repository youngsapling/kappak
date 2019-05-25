package com.ysl.kappak.config.kappakconfig;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;
import com.ysl.kappak.config.kappakconfig.dto.RetryerRegistry;

import java.util.concurrent.TimeUnit;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:07
 * @modifyTime :
 * @description : 默认的配置器
 */
public class DefaultKappakConfig implements KappakConfig {
    @Override
    public void addReTryEr(RetryerRegistry retryerRegistry) {
        Retryer retryer = RetryerBuilder
                .<Boolean>newBuilder()
                //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()
                //返回false也需要重试
                .retryIfResult(Predicates.equalTo(false))
                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                //尝试次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(20))
                .build();
        retryerRegistry.setRetryer(retryer);
    }
}
