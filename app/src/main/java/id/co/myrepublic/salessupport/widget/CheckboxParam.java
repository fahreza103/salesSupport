package id.co.myrepublic.salessupport.widget;

/**
 * POJO checkbox to store checkbox value and checked state
 *
 * @author Fahreza Tamara
 */

public class CheckboxParam {

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
