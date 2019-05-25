package kappak.config.kappakconfig;

import kappak.config.kappakconfig.dto.ParamResolverRegistry;
import kappak.config.kappakconfig.dto.UriSelectorRegistry;

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

    default void addMethodParameterResolver(ParamResolverRegistry paramResolverRegistry){

    }

}
