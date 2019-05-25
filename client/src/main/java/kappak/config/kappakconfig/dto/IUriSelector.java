package kappak.config.kappakconfig.dto;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:28
 * @description :
 */
public interface IUriSelector {
    default HandlerMethod select(String uri, RequestMappingHandlerMapping handlerMapping){
        return null;
    }
}
