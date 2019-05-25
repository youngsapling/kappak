package com.ysl.kappak.config.kappakconfig;

import com.ysl.kappak.config.kappakconfig.dto.RetryerRegistry;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:03
 * @modifyTime :
 * @description :
 */
@Data
@Component
public class KappakConfigWrapper implements ApplicationContextAware, ApplicationRunner {
    private ApplicationContext ac;
    private RetryerRegistry retryerRegistry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, KappakConfig> kappakConfigMap = ac.getBeansOfType(KappakConfig.class);
        KappakConfig kappakConfig = null;
        if(CollectionUtils.isEmpty(kappakConfigMap)){
            kappakConfig = new DefaultKappakConfig();
        }else {
            for (KappakConfig mvcConfig : kappakConfigMap.values()){
                kappakConfig = mvcConfig;
            }
        }
        kappakConfig.addReTryEr(new RetryerRegistry());
    }
}
