package kappak.config.kappakconfig.resolver;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:47
 * @modifyTime :
 * @description : 默认参数解析器.
 */
@Order
@Data
@Component
public class DefaultParamResolver implements IParamResolver {

    private String name ;
    public DefaultParamResolver(){
        this.name = "i'm defaultParamResolver";
    }
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
