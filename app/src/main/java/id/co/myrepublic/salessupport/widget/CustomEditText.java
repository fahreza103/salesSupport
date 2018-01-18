package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.DatePickerBuilder;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.StringUtil;

import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_DATE;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_EMAIL;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_REQUIRED;

/**
 * Custom editview with validation implementations and label text
 *
 * @author Fahreza Tamara
 */

public class CustomEditText extends LinearLayout {

    private List<String> validators = new ArrayList<String>();
    private Boolean hasRequiredValidation = false;
    private String dateFormat;

    private TextView labelText;
    private EditText editText;
    private Context context;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.customview_edittext, this);

        int[] sets = {R.attr.validator, R.attr.labelText,R.attr.inputType,R.attr.clickable,R.attr.dateFormat,R.attr.inputHeight,R.attr.value,R.attr.inputHint,R.attr.enabled};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);

        String attrValidator = typedArray.getString(R.styleable.appAttr_validator);
        String attrLabelText = typedArray.getString(R.styleable.appAttr_labelText);
        String attrInputType = typedArray.getString(R.styleable.appAttr_inputType);
        String attrInputValue = typedArray.getString(R.styleable.appAttr_value);
        Boolean attrClickable = typedArray.getBoolean(R.styleable.appAttr_clickable,true);
        Boolean attrEnabled = typedArray.getBoolean(R.styleable.appAttr_enabled,true);
        String attrDateFormat  = typedArray.getString(R.styleable.appAttr_dateFormat);
        String attrInputHint = typedArray.getString(R.styleable.appAttr_dateFormat);
        int attrInputHeight = typedArray.getDimensionPixelSize(R.styleable.appAttr_inputHeight,0);


        typedArray.recycle();

        labelText = (TextView) findViewById(R.id.custom_edittext_label);
        editText  = (EditText) findViewById(R.id.custom_edittext_input);

        setValidator(attrValidator);
        setLabelText(attrLabelText);
        setDateFormat(attrDateFormat);
        setInputType(attrInputType);
        setInputValue(attrInputValue);
        setEditTextClickable(attrClickable);
        setInputHeight(attrInputHeight);
        setInputHint(attrInputHint);
        setFormEnabled(attrEnabled);
    }

    /**
     *  This is to save the current state of this component after going into the next fragment / activity
     *
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString("editTextValue", getInputValue());
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

    /*
       https://stackoverflow.com/questions/25998596/how-to-retrieve-state-of-custom-views
       The reason this occurs is that the EditText within each instance of your ComplexView shares an ID with every other instance of that View.
       When your ComplexView dispatches save/restore events to it's children, things get a little funky with Views that share the same ID.
       
     */
    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }
    //================

    /**
     * if this edittext has date validator, set the format here, if not specified , default format dd/MM/yyyy will be used
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


    /**
     * Add the validation into edittext
     * @param validator
     */
    public void addValidator (String validator) {
        validators.add(validator);
    }

    /**
     * Get the validators
     * @return
     */
    public List<String> getValidators() {
        return this.validators;
    }

    /**
     * Set Error for fail validation
     * @param message
     */
    public void setError(String message) {
        this.editText.setError(message);
    }

    /**
     * Set Error for fail validation with custom icon
     * @param message
     * @param drawable
     */
    public void setError(String message, Drawable drawable) {
        this.editText.setError(message,drawable);
    }

    /**
     * Set editText height
     * @param height
     */
    public void setInputHeight(int height) {
        if(height > 0)
          editText.getLayoutParams().height = (int) height;
    }

    public void setInputHint(String hint) {
        editText.setHint(hint);
    }

    public void setFormEnabled(Boolean enabled) {
        KeyListener mKeyListener = editText.getKeyListener();
        if(enabled) {
            editText.setKeyListener(mKeyListener);
            editText.setFocusable(true);
            editText.setEnabled(true);
        } else {
            editText.setKeyListener(null);
            editText.setFocusable(false);
            editText.setEnabled(false);
        }
    }

    /**
     * perform validation for this editText
     * @return true if this edittext pass the validation or no validation defined
     */
    public Boolean validate() {
        if (validators.size() == 0) return true;
        return Validator.validate(this) == 0 ? true : false;
    }

    public void setLabelText(String text) {
        if(hasRequiredValidation) {
            text+="*";
        }
        labelText.setText(text);
    }


    /**
     * Set validator for this editText, if multiple validator defined, use comma as seperator
     * EX : required,email
     * @param validatorAttr
     */
    public void setValidator(String validatorAttr) {
        this.hasRequiredValidation = false;
        if(!StringUtil.isEmpty(validatorAttr)) {
            String[] validatorArr = validatorAttr.split(",");
            for(String validator : validatorArr) {
                if(VALIDATION_REQUIRED.equals(validator)) {
                    this.validators.add(validator);
                    this.hasRequiredValidation = true;
                } else if(VALIDATION_EMAIL.equals(validator)) {
                    this.validators.add(validator);
                } else if(VALIDATION_DATE.equals(validator)) {
                    this.validators.add(validator);
                }
            }
        } else {
            this.validators.clear();
        }
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

    public void setEditTextClickable(Boolean clickable) {
        this.editText.setClickable(clickable);
    }

    public String getLabelText() {
        return labelText.getText().toString();
    }

    public String getInputValue() {
        return editText.getText().toString();
    }

    public String getError() {
        return editText.getError() == null ? null : editText.getError().toString();
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setInputOnFocusChangeListener(OnFocusChangeListener listener) {
        editText.setOnFocusChangeListener(listener);
    }

    public void setInputAnimation(Animation animation) {
        animation.setDuration(500);
        editText.startAnimation(animation);
    }

}
