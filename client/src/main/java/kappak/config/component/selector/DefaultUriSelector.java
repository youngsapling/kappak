package kappak.config.component.selector;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:32
 * @modifyTime :
 * @description :
 */
public class DefaultUriSelector implements IUriSelector {

    @Override
    public HandlerMethod select(String uri, RequestMappingHandlerMapping handlerMapping){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> iterator = handlerMethods.entrySet().iterator();
        HandlerMethod hm = null;
        while (iterator.hasNext()) {
            Map.Entry<RequestMappingInfo, HandlerMethod> entry = iterator.next();
            RequestMappingInfo requestMappingInfo = entry.getKey();
            Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            if (patterns.contains(uri)) {
                hm = entry.getValue();
                break;
            }
        }
        return hm;
    }
}
