package kappak.config.kappakconfig.dto;

import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:44
 * @description :
 */
public interface IParamResolver {
    /**
     * 具体解析hm方法参数的方法
     * @param handlerMethod
     * @param questParam
     * @return
     */
    List parseParam(HandlerMethod handlerMethod, String questParam);
}
