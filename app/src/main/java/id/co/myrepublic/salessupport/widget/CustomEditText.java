package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.StyleableRes;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.StringUtil;

import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_DATE;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_EMAIL;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_REQUIRED;

/**
 * Custom editview with validation implementations and label text
 */

public class CustomEditText extends LinearLayout {

    @StyleableRes
    int index0 = 0;
    @StyleableRes
    int index1 = 1;
    @StyleableRes
    int index2 = 2;
    @StyleableRes
    int index3 = 3;
    @StyleableRes
    int index4 = 4;
    @StyleableRes
    int index5 = 5;

    private List<String> validators = new ArrayList<String>();
    private Boolean hasRequiredValidation = false;
    private String dateFormat;

    private TextView labelText;
    private EditText editText;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.customview_edittext, this);

        int[] sets = {R.attr.validator, R.attr.labelText,R.attr.inputType,R.attr.inputValue,R.attr.clickable};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);

        String attrValidator = typedArray.getString(index0);
        String attrLabelText = typedArray.getString(index1);
        String attrInputType = typedArray.getString(index2);
        String attrInputValue = typedArray.getString(index3);
        Boolean attrClickable = typedArray.getBoolean(index4,true);
        Float attrLabelWidth  = typedArray.getDimension(index5,0);

        typedArray.recycle();

        labelText = (TextView) findViewById(R.id.custom_edittext_label);
        editText  = (EditText) findViewById(R.id.custom_edittext_input);

        setValidator(attrValidator);
        setLabelText(attrLabelText);
        setInputType(attrInputType);
        setInputValue(attrInputValue);
        setEditTextClickable(attrClickable);
    }

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
     * perform validation for this editText
     * @return true if this edittext pass the validation or no validation defined
     */
    public Boolean validate() {
        if (validators.size() == 0) return true;
        return Validator.validate(this);
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

    public void setInputType(String inputTypeAttr) {
        int inputType = InputType.TYPE_CLASS_TEXT;
        if("text".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_CLASS_TEXT;
        } else if("phone".equals(inputTypeAttr)) {
            inputType = InputType.TYPE_CLASS_PHONE;
        } else if("none".equals(inputType)) {
            inputType = InputType.TYPE_NULL;
        } else if("textMultiline".equals(inputType)) {
            inputType = InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE;
        } else if("calendar".equals(inputType)) {
            inputType = InputType.TYPE_NULL;
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

    public String getInputText() {
        return editText.getText().toString();
    }

    public String getError() {
        return editText.getError() == null ? null : editText.getError().toString();
    }

    public String getDateFormat() {
        return this.dateFormat;
    }


}
