package kappak.config.component;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.annotation.Annotation;
import java.util.Enumeration;

/**
 * @author: youngsapling
 * @date: 2019-05-28
 * @modifyTime:
 * @description:
 */
@Component
public class YoungArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Young.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Young young = parameter.getParameterAnnotation(Young.class);

        String alias = getAlias(young, parameter);

        //拿到obj, 先从ModelAndViewContainer中拿, 若没有则new 1个参数类型的实例
        Object obj = (mavContainer.containsAttribute(alias)) ?
                mavContainer.getModel().get(alias) : createAttribute(alias, parameter, binderFactory, webRequest);


        //获得WebDataBinder，这里的具体WebDataBinder是ExtendedServletRequestDataBinder
        WebDataBinder binder = binderFactory.createBinder(webRequest, obj, alias);

        Object target = binder.getTarget();

        if(target != null) {
            //绑定参数
            bindParameters(webRequest, binder, alias);
            //JSR303 验证
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors()) {
                if (isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
        }

        return target;
    }


    private Object createAttribute(String alias, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest webRequest) {
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    private void bindParameters(NativeWebRequest request, WebDataBinder binder, String alias) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        // 这个包要从pom导入
        MockHttpServletRequest newRequest = new MockHttpServletRequest();

        Enumeration<String> enu = servletRequest.getParameterNames();
        while(enu.hasMoreElements()) {
            String paramName = enu.nextElement();
            if(paramName.equals(alias)) {
                newRequest.setParameter(alias, request.getParameter(paramName));
            }
        }
        ((ExtendedServletRequestDataBinder)binder).bind(newRequest);
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
                break;
            }
        }
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));

        return !hasBindingResult;
    }

    private String getAlias(Young Young, MethodParameter parameter) {
        //得到Young的属性value，也就是对象参数的简称
        String alias = Young.value();
        if(alias == null || Strings.isBlank(alias)) {
            //如果简称为空，取对象简称的首字母小写开头
            String simpleName = parameter.getParameterType().getSimpleName();
            alias = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        }
        return alias;
    }
}
