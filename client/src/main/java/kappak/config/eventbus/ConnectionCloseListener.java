package kappak.config.eventbus;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import com.google.common.eventbus.Subscribe;
import kappak.websocket.KappakSocketClientBuilder;
import kappak.controller.ConsoleController;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: youngsapling
 * @date: 2019-05-31
 * @modifyTime:
 * @description: cilent端断开的监听器, 使用Guava-EventBus
 */
@Slf4j
@Component
public class ConnectionCloseListener {
    @Autowired
    ConsoleController consoleController;
    @Autowired
    KappakSocketClientBuilder builder;

    @Subscribe
    public void onEvent(ConnectionCloseEvent event) {
        if(!event.getReTry()){
            return;
        }
        WebSocketClient webSocketClient;
        try {
            webSocketClient = builder.build();
            // 构造任务
            Callable<Boolean> call = () -> {
                webSocketClient.connect();
                return true;
            };
            // 重试机制
            Retryer retryer = RetryerBuilder
                    .<Boolean>newBuilder()
                    //返回false也需要重试
                    .retryIfResult(Predicates.equalTo(false))
                    //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                    .retryIfException()
                    //重调策略
                    .withWaitStrategy(WaitStrategies.fixedWait(1000, TimeUnit.MILLISECONDS))
                    //尝试次数
                    .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                    .build();
            retryer.call(call);
        } catch (ExecutionException | RetryException e) {
            log.error("auto reConnection fail.");
            e.printStackTrace();
        }

    }
}
