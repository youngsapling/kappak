package kappak.config.kappakconfig.selector;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:28
 * @description :
 */
public interface IUriSelector {
    /**
     * @param uri
     * @param handlerMapping
     * @return
     */
    default HandlerMethod select(String uri, RequestMappingHandlerMapping handlerMapping){
        return null;
    }
}
