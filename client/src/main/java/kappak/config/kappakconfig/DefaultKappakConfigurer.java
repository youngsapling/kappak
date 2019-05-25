package kappak.config.kappakconfig;


import kappak.config.kappakconfig.dto.DefaultParamResolver;
import kappak.config.kappakconfig.dto.DefaultUriSelector;
import kappak.config.kappakconfig.dto.ParamResolverRegistry;
import kappak.config.kappakconfig.dto.UriSelectorRegistry;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 14:07
 * @modifyTime :
 * @description : 默认的配置器
 */
public class DefaultKappakConfigurer implements KappakConfigurer {

    @Override
    public void addUrISelector(UriSelectorRegistry uriSelectorRegistry) {
        uriSelectorRegistry.setUriSelector(new DefaultUriSelector());
    }
    @Override
    public void addMethodParameterResolver(ParamResolverRegistry paramResolverRegistry){
        paramResolverRegistry.setParamResolver(new DefaultParamResolver());
    }
}
