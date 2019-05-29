package kappak.test;

import com.alibaba.fastjson.JSONObject;
import kappak.config.component.resolver.IParamResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:47
 * @modifyTime :
 * @description : 测试排序用的参数解析器.
 */
@Order(value = 121545454)
@Component
public class TestParamResolver implements IParamResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, String requestParam) {
        Class<?> parameterType = parameter.getParameterType();
        Object args = JSONObject.parseObject(requestParam, parameterType);
        return args;
    }
}
