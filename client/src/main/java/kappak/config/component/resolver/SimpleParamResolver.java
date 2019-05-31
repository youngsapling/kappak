package kappak.config.component.resolver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author: youngsapling
 * @date: 2019-05-29
 * @modifyTime:
 * @description: 自定义对RequestParam注解的解析
 */
@Order(value = Ordered.LOWEST_PRECEDENCE - 100)
@Data
@Component
public class SimpleParamResolver implements IParamResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        RequestParam parameterAnnotation = parameter.getParameterAnnotation(RequestParam.class);
        return null != parameterAnnotation.getClass();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, String requestParam) {
        Map map = JSONObject.parseObject(requestParam, Map.class);
        // 拿到方法参数的变量名
        String parameterName;
        if(Strings.isNullOrEmpty(parameter.getParameterAnnotation(RequestParam.class).name())){
            // 注解没有指定变量名, 使用真实变量名
            ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
            String[] parameterNames = pnd.getParameterNames(parameter.getMethod());
            int parameterIndex = parameter.getParameterIndex();
            parameterName = parameterNames[parameterIndex];
        }else {
            // 注解指定了变量名
            parameterName = parameter.getParameterAnnotation(RequestParam.class).name();
        }
        JSONArray jsonArrayValue = (JSONArray)map.get(parameterName);
        Class<?> parameterType = parameter.getParameterType();
        Object targetValue = jsonArrayValue.getObject(0, parameterType);
        return targetValue;
    }
}
