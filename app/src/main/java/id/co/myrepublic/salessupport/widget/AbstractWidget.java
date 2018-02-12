package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.Validator;
import id.co.myrepublic.salessupport.util.StringUtil;

import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_DATE;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_EMAIL;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_PHONE;
import static id.co.myrepublic.salessupport.support.Validator.VALIDATION_REQUIRED;

/**
 * Abstract implementation of custom widget, contains common attribute handling, and validation
 *
 * @author Fahreza Tamara
 */

public abstract class AbstractWidget extends LinearLayout {

    /** tag for checkboxes, used to get single view by tag */
    public static final String CHECKBOX_TAG = "checkbox";
    /** text for empty spinner value */
    public static final String SPINNER_EMPTY_TEXT = "[select]";
    /** text to show then spinner item is in load */
    public static final String SPINNER_LOADING_TEXT = "Loading Item...";


    protected Context context;
    protected List<String> validators = new ArrayList<String>();
    protected Boolean hasRequiredValidation = false;
    protected TextView labelText;
    protected String dateFormat;
    protected View widgetView;
    protected String errorMsg;


    public AbstractWidget(Context context) {
        super(context);
        this.context = context;
    }

    public AbstractWidget(Context context, AttributeSet attrs, Integer layout) {
        super(context, attrs);
        init(context,attrs,layout,null);
    }

    public AbstractWidget(Context context, AttributeSet attrs, Integer layout, Integer inputId) {
        super(context, attrs);
        init(context,attrs,layout,inputId);
    }

    private void init(Context context, AttributeSet attrs, Integer layout, Integer inputId) {
        inflate(context,layout,this);

        this.context = context;
        if(inputId != null)
            this.widgetView = findViewById(inputId);

        initBaseAttr(context, attrs);
        initCustomAttr(context,attrs);
    }


    /**
     * Init basic attribute and inflate the layout
     * @param context
     * @param attrs
     */
    protected void initBaseAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.appAttr);

        String attrValidator = typedArray.getString(R.styleable.appAttr_validator);
        String attrLabelText = typedArray.getString(R.styleable.appAttr_labelText);
        Boolean attrClickable = typedArray.getBoolean(R.styleable.appAttr_clickable,true);
        Boolean attrEnabled = typedArray.getBoolean(R.styleable.appAttr_enabled,true);
        String attrDateFormat  = typedArray.getString(R.styleable.appAttr_dateFormat);
        int attrInputHeight = typedArray.getDimensionPixelSize(R.styleable.appAttr_inputHeight,0);


        typedArray.recycle();

        // make sure all custom widget has same id name for this textView
        labelText = (TextView) findViewById(R.id.custom_widget_label);

        // Common method
        setValidator(attrValidator);
        setLabelText(attrLabelText);
        setDateFormat(attrDateFormat);
        setInputClickable(attrClickable);
        setInputHeight(attrInputHeight);
        setInputEnabled(attrEnabled);
    }


    /**
     * Set validator for this widget, if multiple validator defined, use comma as seperator
     * EX : required,email
     * @param validatorAttr
     */
    protected void setValidator(String validatorAttr) {
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
                } else if(VALIDATION_PHONE.equals(validator)) {
                    this.validators.add(validator);
                }
            }
        } else {
            this.validators.clear();
        }
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
     * validate the input widget
     * @return
     */
    public Boolean validate() {
        return Validator.validateSingleComponent(this);
    }


    /**
     * Set label for the widget
     * @param text
     */
    public void setLabelText(String text) {
        if(hasRequiredValidation) {
            text+="*";
        }
        labelText.setText(text);
    }

    public String getLabelText() {
        return labelText.getText().toString();
    }

    /**
     * if this edittext has date validator, set the format here, if not specified , default format dd/MM/yyyy will be used
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * get date format
     */
    public String getDateFormat() {
        return this.dateFormat;
    }


    /**
     * Set enabled state
     * @param attrEnabled
     */
    public void setInputEnabled(Boolean attrEnabled) {
        widgetView.setEnabled(attrEnabled);
        widgetView.setFocusable(attrEnabled);
    }

    /**
     * Set height for the input widget
     * @param attrInputHeight
     */
    public void setInputHeight(int attrInputHeight) {
        if(attrInputHeight > 0)
            widgetView.getLayoutParams().height = (int) attrInputHeight;
    }

    /**
     * Set clickable for the input widget
     * @param attrClickable
     */
    public void setInputClickable(Boolean attrClickable) {
        widgetView.setClickable(attrClickable);
    }

    /**
     * Set animation for the input widget
     * @param animation
     * @param duration
     */
    public void setInputAnimation(Animation animation, Integer duration) {
        animation.setDuration(duration);
        widgetView.startAnimation(animation);
    }

    /**
     * Request focus for input widget
     * @return
     */
    public Boolean requestInputFocus() {
        return widgetView.requestFocus();
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
     * Init custom attr , every widget should has own attribute
     * @param context
     * @param attrs
     */
    protected abstract void initCustomAttr(Context context, AttributeSet attrs);


    /**
     * Set input error for fail validation
     * @param message
     */
    public abstract void setError(String message);

    /**
     * Set input error for fail validation and custom drawable
     * @param message
     * @param drawable
     */
    public abstract void setError(String message, Drawable drawable);

    /**
     * get error message for fail validation
     * @return
     */
    public abstract String getError();

    /**
     * Get input widget value
     * @return
     */
    public abstract Object getInputValue();

}
