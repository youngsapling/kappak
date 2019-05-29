package kappak.config.component.selector;

import lombok.Data;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/25 15:02
 * @modifyTime :
 * @description :
 */
@Data
public class UriSelectorRegistry {
    private IUriSelector uriSelector;

    public void addUriSelector(IUriSelector uriSelector){
        this.uriSelector = uriSelector;
    }
}
