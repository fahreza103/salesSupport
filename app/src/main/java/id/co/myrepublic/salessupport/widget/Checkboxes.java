package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import id.co.myrepublic.salessupport.R;

/**
 * Collection of checkbox
 *
 * @author Fahreza Tamara
 */

public class Checkboxes extends AbstractWidget {

    public Checkboxes(Context context) {
        super(context);
    }

    public Checkboxes(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.customview_spinner,R.id.custom_spinner_box);
    }

    @Override
    protected void initCustomAttr(Context context, AttributeSet attrs) {

    }

    @Override
    public void setError(String message) {

    }

    @Override
    public void setError(String message, Drawable drawable) {

    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public Object getInputValue() {
        return null;
    }
}
