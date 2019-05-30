package com.ysl.kappak.request;

import com.google.common.base.Strings;
import com.ysl.kappak.util.Contants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/23 21:04
 * @modifyTime :
 * @description : 通过过滤器偷换掉请求.
 */
@Slf4j
@WebFilter(filterName = "httpServletRequestFilter", urlPatterns = { "/*" })
public class HttpServletRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("httpServletRequestFilter init();");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest){
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String methodType = request.getMethod();
            String clientName = request.getHeader("clientName");
            if(Strings.isNullOrEmpty(clientName)){
                throw new RuntimeException("clientName can't null");
            }
            if(Contants.POST.toLowerCase().equals(methodType.toLowerCase()) && !Strings.isNullOrEmpty(clientName)){
                requestWrapper = new RequestBodyHttpServletRequestWrapper(request);
            }
        }
        if(null == requestWrapper){
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }
}
