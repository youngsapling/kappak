package kappak.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public String dispatcher(String uri, String json) throws InvocationTargetException, IllegalAccessException {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Iterator<?> iterator = handlerMethods.entrySet().iterator();
        // 第一版简单匹配获取hm
        HandlerMethod hm = null;
        while (iterator.hasNext()) {
            Map.Entry<RequestMappingInfo, HandlerMethod> entry = (Map.Entry) iterator.next();
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            if (patterns.contains(uri)) {
                hm = entry.getValue();
                break;
            }
        }
        if (hm == null) {
            return null;
        }
        Class<?> beanType = hm.getBeanType();
        Object beanController = applicationContext.getBean(beanType);
        MethodParameter[] methodParameters = hm.getMethodParameters();
        Class<?> parameterType = null;
        // 暂时只支持方法参数只有一个对象.
        for (MethodParameter mp : methodParameters) {
            parameterType = mp.getParameterType();
        }
        Object args = JSONObject.parseObject(json, parameterType);
        Object invoke = hm.getMethod().invoke(beanController, args);
        return JSON.toJSONString(invoke);
    }

}
