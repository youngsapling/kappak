package com.ysl.kappak.config.kappakconfig.dto;

import com.github.rholder.retry.Retryer;
import lombok.Data;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:12
 * @modifyTime :
 * @description :
 */
@Data
public class RetryerRegistry {
    private Retryer retryer;
}
