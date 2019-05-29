package kappak.config.component.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:44
 * @description :
 */
public interface IParamResolver extends HandlerMethodArgumentResolver {

    /**
     * 判断是否支持解析该参数方法.
     * @param parameter
     * @return
     */
    @Override
    boolean supportsParameter(MethodParameter parameter);
    /**
     * 具体解析hm方法参数的方法.
     * @param parameter
     * @param requestParam
     * @return
     */
    Object resolveArgument(MethodParameter parameter, String requestParam);

    /**
     * 这种方式没有创建request.用不到.
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    default Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
        return null;
    }
}
