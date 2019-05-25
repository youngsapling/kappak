package kappak.config.kappakconfig.dto;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:47
 * @modifyTime :
 * @description : 默认参数解析器.
 */
public class DefaultParamResolver implements IParamResolver{

    @Override
    public List parseParam(HandlerMethod handlerMethod, String requestParam) {
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        Class<?> parameterType = null;
        // 暂时只支持方法参数只有一个对象.
        for (MethodParameter mp : methodParameters) {
            parameterType = mp.getParameterType();
        }
        Object args = JSONObject.parseObject(requestParam, parameterType);
        List list = new ArrayList(1);
        list.add(args);
        return list;
    }
}
