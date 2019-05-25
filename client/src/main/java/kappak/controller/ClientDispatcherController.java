package kappak.controller;

import com.alibaba.fastjson.JSON;
import kappak.config.kappakconfig.KappakConfigWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/18 16:29
 * @modifyTime :
 * @description : 客户端分发器
 */
@RestController
@RequestMapping("/clientController")
public class ClientDispatcherController {
    @Autowired
    DispatcherServlet dispatcherServlet;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    RequestMappingHandlerMapping handlerMapping;
    @Autowired
    KappakConfigWrapper kappakConfigWrapper;

    public String dispatcher(String uri, String json) throws InvocationTargetException, IllegalAccessException {
        // 调用uri映射器
        HandlerMethod hm = kappakConfigWrapper.getUriSelectorRegistry().getUriSelector().select(uri, handlerMapping);
        if (hm == null) {
            return null;
        }
        Class<?> beanType = hm.getBeanType();
        Object beanController = applicationContext.getBean(beanType);
        // 调用方法参数解析器
        List args = kappakConfigWrapper.getParamResolverRegistry().getParamResolver().parseParam(hm, json);
        Object invoke = hm.getMethod().invoke(beanController, args.toArray());
        return JSON.toJSONString(invoke);
    }
}
