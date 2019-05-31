package kappak.config.eventbus;

import lombok.Data;

/**
 * @author: youngsapling
 * @date: 2019-05-31
 * @modifyTime:
 * @description: WebSocketClient端连接关闭事件.
 */
@Data
public class ConnectionCloseEvent {
    Boolean reTry;

    public ConnectionCloseEvent(boolean reTry){
        this.reTry = reTry;
    }
}
