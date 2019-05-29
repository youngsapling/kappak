package kappak.test;

import kappak.config.kappakconfig.KappakConfigurer;
import kappak.config.component.resolver.ParamResolverRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: youngsapling
 * @date: 2019-05-29
 * @modifyTime:
 * @description: 测试自定义组件类.
 */
@Component
public class TestKappakConfigurer implements KappakConfigurer {
    @Autowired
    TestParamResolver testParamResolver;
    /**
     * 参数解析器
     * @param paramResolverRegistry
     */
    @Override
    public void addMethodParameterResolver(ParamResolverRegistry paramResolverRegistry){
        paramResolverRegistry.addParamResolver(testParamResolver);
    }
}
