package com.ysl.kappak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/20 22:18
 * @modifyTime :
 * @description : copy from https://blog.csdn.net/j903829182/article/details/78342941?tdsourcetag=s_pctim_aiomsg
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/server").setViewName("/server");
        registry.addViewController("/chat").setViewName("/chat");
    }
}
