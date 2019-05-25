package kappak.config.kappakconfig;

import kappak.config.kappakconfig.dto.ParamResolverRegistry;
import kappak.config.kappakconfig.dto.UriSelectorRegistry;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:03
 * @modifyTime :
 * @description : 自动装载自定义组件.
 */
@Data
@Component
public class KappakConfigWrapper implements ApplicationContextAware, ApplicationRunner {
    private ApplicationContext ac;
    private UriSelectorRegistry uriSelectorRegistry;
    private ParamResolverRegistry paramResolverRegistry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        uriSelectorRegistry = new UriSelectorRegistry();
        paramResolverRegistry = new ParamResolverRegistry();
        // 默认的执行一遍
        KappakConfigurer defaultKappakConfigurer = new DefaultKappakConfigurer();
        defaultKappakConfigurer.addUrISelector(uriSelectorRegistry);
        defaultKappakConfigurer.addMethodParameterResolver(paramResolverRegistry);

        // 用户自定义的执行一遍
        Map<String, KappakConfigurer> kappakConfigMap = ac.getBeansOfType(KappakConfigurer.class);
        KappakConfigurer userKappakConfigurer = null;
        if(!CollectionUtils.isEmpty(kappakConfigMap)){
            for (KappakConfigurer mvcConfig : kappakConfigMap.values()){
                userKappakConfigurer = mvcConfig;
            }
        }
        if(Objects.nonNull(userKappakConfigurer)){
            userKappakConfigurer.addUrISelector(uriSelectorRegistry);
            userKappakConfigurer.addMethodParameterResolver(paramResolverRegistry);
        }
    }
}
