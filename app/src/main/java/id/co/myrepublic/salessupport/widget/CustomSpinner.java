package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

import android.widget.TextView;

import id.co.myrepublic.salessupport.R;


/**
 * Custom spinner with label Text
 */

public class CustomSpinner extends AppCompatSpinner {

    private TextView labelText;

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.customview_spinner, this);

        int[] sets = {R.attr.labelText};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);
        labelText = (TextView) findViewById(R.id.custom_spinner_label);

        String attrLabelText = typedArray.getString(R.styleable.appAttr_labelText);
        typedArray.recycle();

        setLabelText(attrLabelText);
    }

    public void setLabelText(String text) {
        labelText.setText(text);
    }

    public CharSequence getLabelText() {
        return labelText.getText();
    }



}
