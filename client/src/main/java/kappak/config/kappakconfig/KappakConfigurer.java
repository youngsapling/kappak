package kappak.config.kappakconfig;

import kappak.config.component.resolver.ParamResolverRegistry;
import kappak.config.component.selector.UriSelectorRegistry;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:24
 * @description : see WebMvcConfigurer
 */
public interface KappakConfigurer {

    /**
     * 添加自定义uri映射 HandlerMethod 规则
     * @param uriSelectorRegistry
     */
    default void addUrISelector(UriSelectorRegistry uriSelectorRegistry){

    }

    /**
     * 参数解析器
     * @param paramResolverRegistry
     */
    default void addMethodParameterResolver(ParamResolverRegistry paramResolverRegistry){

    }

}
