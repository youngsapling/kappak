package com.ysl.kappak.util;

import lombok.extern.slf4j.Slf4j;

/**
 *  高效GUID产生算法(sequence),基于Snowflake实现64位自增ID算法
 *  优化开源项目 http://git.oschina.net/yu120/sequence
 * @author: youngsapling
 * @date: 2019-03-07
 * @modifyTime:
 * @description:
 */
@Slf4j
public class IdWorker {
    private static long workerId = 5L;
    private static long dataCenterId = 5L;
    private static long sequence = 0L;

    private static long twepoch = 1288834974657L;
    private static long workerIdBits = 5L;
    private static long dataCenterIdBits = 5L;
    private static long sequenceBits = 12L;
    private static long workerIdShift = sequenceBits;
    private static long dataCenterIdShift = sequenceBits + workerIdBits;
    private static long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private static long sequenceMask = 4096L;
    private static long lastTimestamp = -1L;

    private static synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            log.error(
                    "clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;

        // 这儿就是最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit那儿；将机房id左移12位放到5 bit那儿；将机器id左移17位放到5 bit那儿；将序号放最后12 bit
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return ((timestamp - twepoch) << timestampLeftShift) |
                (dataCenterId << dataCenterIdShift) |
                (workerId << workerIdShift) | sequence;
    }

    public static Long createId(){
        return nextId();
    }

    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis();
    }
}