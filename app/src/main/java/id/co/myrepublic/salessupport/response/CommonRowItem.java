package id.co.myrepublic.salessupport.response;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Used to populate text in row adapter on listView
 */

public class CommonRowItem implements Serializable {

    private String mainText;
    private String mainText2;
    private String subText;
    private String subText2;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getMainText2() {
        return mainText2;
    }

    public void setMainText2(String masinText2) {
        this.mainText2 = masinText2;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getSubText2() {
        return subText2;
    }

    public void setSubText2(String subText2) {
        this.subText2 = subText2;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
