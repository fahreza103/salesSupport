package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.DatePickerBuilder;

/**
 * Custom editview with validation implementations and label text
 *
 * @author Fahreza Tamara
 */
public class CustomEditText extends AbstractWidget {


    private EditText editText;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.customview_edittext,R.id.custom_edittext_input);
    }

    @Override
    protected void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.appAttr);

        String attrInputType = typedArray.getString(R.styleable.appAttr_inputType);
        String attrInputHint = typedArray.getString(R.styleable.appAttr_dateFormat);
        String attrInputValue = typedArray.getString(R.styleable.appAttr_value);

        typedArray.recycle();
        editText  = (EditText) findViewById(R.id.custom_edittext_input);

        setInputType(attrInputType);
        setInputHint(attrInputHint);
        setInputValue(attrInputValue);
    }

    public void setInputHint(String hint) {
        editText.setHint(hint);
    }

    public void setInputType(int inputType) {
        this.editText.setInputType(inputType);
    }

    /**
     * set Input type of edittext
     * @param inputTypeAttr
     */
    public void setInputType(String inputTypeAttr) {
        int inputType = InputType.TYPE_CLASS_TEXT;
        if("text".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_CLASS_TEXT;
        } else if("phone".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_CLASS_PHONE;
        } else if("none".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_NULL;
        } else if("textMultiline".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE;
            editText.setGravity(Gravity.TOP);
        } else if("calendar".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_NULL;
            new DatePickerBuilder(this.editText,this.context,this.dateFormat);
        }
        this.editText.setInputType(inputType);
    }

    public void setInputValue(String value) {
        this.editText.setText(value);
    }

    public void setInputOnFocusChangeListener(OnFocusChangeListener listener) {
        editText.setOnFocusChangeListener(listener);
    }

    @Override
    public void setError(String message) {
        this.editText.setError(message);
    }

    @Override
    public void setError(String message, Drawable drawable) {
        this.editText.setError(message,drawable);
    }

    @Override
    public String getError() {
        return editText.getError() == null ? null : editText.getError().toString();
    }

    @Override
    public Object getInputValue() {
        return editText.getText().toString();
    }

    /**
     *  This is to save the current state of this component after going into the next fragment / activity
     *
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString("editTextValue", getInputValue().toString());
        return bundle;
    }

    /**
     * Restore the state of this component, so the value will be restored and not losing after back from other fragment / activity
     * @param state
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String editTextValue = bundle.getString("editTextValue");
            setInputValue(editTextValue);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}
