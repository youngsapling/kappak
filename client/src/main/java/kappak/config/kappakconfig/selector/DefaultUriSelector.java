package kappak.config.kappakconfig.selector;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:32
 * @modifyTime :
 * @description :
 */
@Data
@Order
@Component
public class DefaultUriSelector implements IUriSelector {
    @Autowired
    private PathMatcher pathMatcher;

    @Override
    public HandlerMethod select(String uri, RequestMappingHandlerMapping handlerMapping){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> iterator = handlerMethods.entrySet().iterator();
        HandlerMethod hm = null;
        while (iterator.hasNext()) {
            Map.Entry<RequestMappingInfo, HandlerMethod> entry = iterator.next();
            RequestMappingInfo requestMappingInfo = entry.getKey();
            Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            for (String pattern : patterns) {
                if (pathMatcher.match(pattern, uri)) {
                    hm = entry.getValue();
                    break;
                }
            }
            if(Objects.nonNull(hm)){
                break;
            }
        }
        return hm;
    }
}
