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
public class DefaultKappakConfigurer implements KappakConfigurer {
    @Override
    public void addReTryEr(RetryerRegistry retryerRegistry) {
        Retryer retryer = RetryerBuilder
                .<Boolean>newBuilder()
                //返回false也需要重试
                .retryIfResult(Predicates.equalTo(false))
                //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()
                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(500, TimeUnit.MILLISECONDS))
                //尝试次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(40))
                .build();
        retryerRegistry.setRetryer(retryer);
    }
}
