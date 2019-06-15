package kappak.websocket;

import kappak.config.component.YoungArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: youngsapling
 * @date: 2019-05-29
 * @modifyTime:
 * @description: client端配置类, 对本项目无用.
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    YoungArgumentResolver youngArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(youngArgumentResolver);
    }
}
