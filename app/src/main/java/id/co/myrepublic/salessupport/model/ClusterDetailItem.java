package id.co.myrepublic.salessupport.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by myrepublicid on 27/9/17.
 */

public class ClusterDetailItem {

    private String key;
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
