package kappak.config.component.resolver;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: youngsapling
 * @date: 2019-05-29
 * @modifyTime:
 * @description:
 */
@Order
@Data
@Component
public class SimpleParamResolver implements IParamResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        if(parameterType == String.class || parameterType == Integer.class){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, String requestParam) {
        Map map = JSONObject.parseObject(requestParam, Map.class);
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        String[] parameterNames = pnd.getParameterNames(parameter.getMethod());
        // todo 构造参数.
        return null;
    }
}
