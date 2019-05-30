package kappak.controller;

import com.alibaba.fastjson.JSON;
import kappak.config.component.resolver.IParamResolver;
import kappak.config.kappakconfig.KappakConfigComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
    KappakConfigComposite kappakConfigComposite;

    public String dispatcher(String uri, String json) {
        // 调用uri映射器
        HandlerMethod hm = kappakConfigComposite.getUriSelectorRegistry().getUriSelector().select(uri, handlerMapping);
        if (hm == null) {
            return null;
        }
        // 调用方法参数解析器
        List args = new ArrayList();
        HandlerMethodArgumentResolver argumentResolver = null;
        MethodParameter[] methodParameters = hm.getMethodParameters();
        for (MethodParameter mp : methodParameters){
            List<IParamResolver> paramResolverList = kappakConfigComposite.getParamResolverRegistry().getParamResolver();
            for (IParamResolver resolver : paramResolverList){
                if(resolver.supportsParameter(mp)){
                    argumentResolver = resolver;
                    break;
                }
            }
            Object targetParam = ((IParamResolver) argumentResolver).resolveArgument(mp, json);
            args.add(targetParam);
        }

        // 获取controller对象.
        Class<?> beanType = hm.getBeanType();
        Object beanController = applicationContext.getBean(beanType);
        // 反射调用
        Object invoke = null;
        try {
            invoke = hm.getMethod().invoke(beanController, args.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(invoke);
    }
}
