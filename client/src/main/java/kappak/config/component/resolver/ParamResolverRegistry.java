package kappak.config.component.resolver;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:43
 * @modifyTime :
 * @description :
 */
@Data
public class ParamResolverRegistry {
    private List<IParamResolver> paramResolver;

    public ParamResolverRegistry(){
        this.paramResolver = new ArrayList<>();
    }

    public void addParamResolver(IParamResolver paramResolver){
        this.paramResolver.add(paramResolver);
    }
}
