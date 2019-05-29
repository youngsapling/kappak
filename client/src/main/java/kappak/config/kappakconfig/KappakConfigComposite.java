package kappak.config.kappakconfig;

import kappak.config.component.resolver.IParamResolver;
import kappak.config.component.resolver.ParamResolverRegistry;
import kappak.config.component.selector.UriSelectorRegistry;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:03
 * @modifyTime :
 * @description : 自动装载自定义组件.
 */
@Data
@Component
public class KappakConfigComposite implements ApplicationContextAware, ApplicationRunner {
    private ApplicationContext ac;
    private UriSelectorRegistry uriSelectorRegistry;
    private ParamResolverRegistry paramResolverRegistry;
    @Autowired
    KappakConfigurer defaultKappakConfigurer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        uriSelectorRegistry = new UriSelectorRegistry();
        paramResolverRegistry = new ParamResolverRegistry();
        // 默认的添加一遍
        defaultKappakConfigurer.addUrISelector(uriSelectorRegistry);
        defaultKappakConfigurer.addMethodParameterResolver(paramResolverRegistry);

        // 用户自定义的添加一遍
        Map<String, KappakConfigurer> kappakConfigMap = ac.getBeansOfType(KappakConfigurer.class);
        if(CollectionUtils.isEmpty(kappakConfigMap)){
            // 没有自定义,
            return;
        }
        KappakConfigurer userKappakConfigurer = null;
        for (KappakConfigurer mvcConfig : kappakConfigMap.values()){
            userKappakConfigurer = mvcConfig;
        }

        if(Objects.nonNull(userKappakConfigurer)){
            userKappakConfigurer.addUrISelector(uriSelectorRegistry);
            userKappakConfigurer.addMethodParameterResolver(paramResolverRegistry);
        }
        // 对添加的解析器List排序
        List<IParamResolver> collect = paramResolverRegistry.getParamResolver().stream().sorted((e1, e2) -> {
            Order a1 = e1.getClass().getAnnotation(Order.class);
            if(Objects.isNull(a1)){
                return 1;
            }
            Order a2 = e2.getClass().getAnnotation(Order.class);
            if(Objects.isNull(a2)){
                return -1;
            }
            return a1.value() - a2.value();
        }).collect(Collectors.toList());
        paramResolverRegistry.setParamResolver(collect);
    }
}
