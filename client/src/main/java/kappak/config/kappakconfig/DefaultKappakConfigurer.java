package kappak.config.kappakconfig;


import kappak.config.component.resolver.DefaultParamResolver;
import kappak.config.component.resolver.SimpleParamResolver;
import kappak.config.component.selector.DefaultUriSelector;
import kappak.config.component.resolver.ParamResolverRegistry;
import kappak.config.component.selector.UriSelectorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:07
 * @modifyTime :
 * @description : 默认的配置器
 */
@Component
public class DefaultKappakConfigurer implements KappakConfigurer {
    @Autowired
    DefaultParamResolver defaultParamResolver;
    @Autowired
    SimpleParamResolver simpleParamResolver;

    @Override
    public void addUrISelector(UriSelectorRegistry uriSelectorRegistry) {
        uriSelectorRegistry.addUriSelector(new DefaultUriSelector());
    }

    @Override
    public void addMethodParameterResolver(ParamResolverRegistry paramResolverRegistry){
        paramResolverRegistry.addParamResolver(defaultParamResolver);
        paramResolverRegistry.addParamResolver(simpleParamResolver);
    }
}
