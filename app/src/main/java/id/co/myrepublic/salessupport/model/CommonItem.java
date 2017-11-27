package id.co.myrepublic.salessupport.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.AppConstant;

/**
 * Created by myrepublicid on 27/9/17.
 */

public class CommonItem {

    @PositionItem(type= AppConstant.ROWITEM_POSITION_MAINTEXT1)
    private String key;
    @PositionItem(type= AppConstant.ROWITEM_POSITION_MAINTEXT2)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
