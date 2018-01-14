package id.co.myrepublic.salessupport.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Common model with default key and value pair
 */

public class CommonItem implements Serializable {

    private static final long serialVersionUID = 33355123454912675L;

    @PositionItem(type= RowItem.MAINTEXT1)
    private String key;
    @PositionItem(type= RowItem.MAINTEXT2)
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
