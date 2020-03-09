package kappak.controller;

import com.alibaba.fastjson.JSON;
import kappak.config.kappakconfig.KappakConfigComposite;
import kappak.config.kappakconfig.resolver.IParamResolver;
import kappak.entity.Bee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/18 16:29
 * @modifyTime :
 * @description : 客户端分发器
 */
@Slf4j
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
    @Autowired
    RestTemplate restTemplate;

    /**
     * 通过反射调用本地方法的形式实现.
     * @param uri
     * @param json
     * @return
     */
    public String reflect(String uri, String json) {
        // 调用uri映射器
        HandlerMethod hm = kappakConfigComposite.getUriSelectorRegistry().getUriSelector().select(uri, handlerMapping);
        if (hm == null) {
            return String.format("this uri {%s} can't been map for server");
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
            invoke = e.getMessage();
        }
        return JSON.toJSONString(invoke);
    }

    /**
     * 通过发送本地的http请求实现伪内存穿透.
     * @param highBee
     * @return
     */
    public String sendRest(Bee highBee, String serverPort) {
        String url = "http://localhost:" + serverPort + highBee.getUri();
        HttpHeaders headers = new HttpHeaders();
        highBee.getRequestHeaders().parallelStream().forEach(header->{
            Map.Entry<String, String> next = header.entrySet().iterator().next();
            headers.add(next.getKey(), next.getValue());
        });

        HttpEntity<String> request = new HttpEntity<>(highBee.getJsonString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request , String.class );
        return response.getBody();
    }

    /**
     * 通过构造MockRequest对象， 将request and response传递。
     */
    public String dispatcher(Bee highBee) {
        String uri = highBee.getUri();
        String json = highBee.getJsonString();
        String httpMethon = highBee.getHttpMethod();
        ServletContext servletContext = dispatcherServlet.getServletConfig().getServletContext();
        //HTTP请求、GET/POST
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        MockHttpServletRequest servletRequest =new MockHttpServletRequest(servletContext);
        // headers
        for (Map<String, String> map : highBee.getRequestHeaders()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                servletRequest.addHeader(entry.getKey(), entry.getValue());
            }
        }
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
        String path = uriComponents.getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        servletRequest.setRequestURI(path);
        servletRequest.setServletPath(path);
        servletRequest.setMethod(httpMethon);
        if (uriComponents.getScheme() != null) {
            servletRequest.setScheme(uriComponents.getScheme());
        }
        if (uriComponents.getHost() != null) {
            servletRequest.setServerName(uriComponents.getHost());
        }
        if (uriComponents.getPort() != -1) {
            servletRequest.setServerPort(uriComponents.getPort());
        }
        try {
            byte[] data = json.getBytes("UTF-8");
            servletRequest.setContent(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String resp = "";
        try {
            dispatcherServlet.service(servletRequest,servletResponse);
            resp = StringUtils.isEmpty(servletResponse.getContentAsString()) ? "" : servletResponse.getContentAsString();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("client result : " + resp);
        return resp;
    }
}
