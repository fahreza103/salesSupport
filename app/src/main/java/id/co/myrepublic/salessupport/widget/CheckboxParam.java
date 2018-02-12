package id.co.myrepublic.salessupport.widget;

import java.io.Serializable;

/**
 * POJO checkbox to store checkbox value and checked state
 *
 * @author Fahreza Tamara
 */

public class CheckboxParam implements Serializable {

    private static final long serialVersionUID = 3243344518456725L;
    public Boolean isChecked;
    public Object value;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
