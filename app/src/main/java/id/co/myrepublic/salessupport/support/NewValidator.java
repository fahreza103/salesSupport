package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.widget.AbstractWidget;
import id.co.myrepublic.salessupport.widget.CustomEditText;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

import static id.co.myrepublic.salessupport.widget.AbstractWidget.EMPTY_SPINNER_TEXT;


/**
 * Class to define all validation implementation, use by custom widget
 *
 * @author Fahreza Tamara
 */

public class NewValidator {

    public static final String VALIDATION_REQUIRED = "required";
    public static final String VALIDATION_EMAIL = "email";
    public static final String VALIDATION_DATE = "date";

    public static final String VALIDATION_KEY_RESULT = "validation_result";

    public static final String VALIDATION_MSG_FAILED = "Validation error, please check your fields";
    public static final String VALIDATION_MSG_REQUIRED = "This field is required";
    public static final String VALIDATION_MSG_EMAIL = "Invalid email format";
    public static final String VALIDATION_MSG_DATE = "Invalid date format";

    private static final String DEFAULT_DATE_FORMAT = "mm/DD/yyyy";

    private static Boolean validateRequired(String value) {
        return !StringUtil.isEmpty(value);
    }

    /**
     * Validate standard email format, xxxxx@xxxx.xxx
     * @param value
     * @return
     */
    private static Boolean validateEmail(String value) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * Validate date, if no format defined, use the default one
     * @param value
     * @param format
     * @return
     */
    private static Boolean validateDate(String value, String format) {
        if(StringUtil.isEmpty(value)) return true;
        try {
            format = format==null ? DEFAULT_DATE_FORMAT : format;
            DateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            df.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * perform validation, private call
     * @param view
     * @return
     */
    private static Boolean validate (String validator, AbstractWidget view) {
        boolean result = true;
        String errorMsg = view.getError()== null ? "": view.getError();
        String newErrorMsg = "";
        String inputValue  = view.getInputValue().toString();
        // Spinner if empty value selected
        if(view instanceof CustomSpinner && EMPTY_SPINNER_TEXT.equals(inputValue)) {
            inputValue = "";
        }

        if(validator.equals(VALIDATION_REQUIRED) && !validateRequired(inputValue)) {
            result = false;
            newErrorMsg += VALIDATION_MSG_REQUIRED;
        } else if(validator.equals(VALIDATION_EMAIL) && !validateEmail(inputValue)) {
            result = false;
            newErrorMsg += VALIDATION_MSG_EMAIL;
        } else if(validator.equals(VALIDATION_DATE) && !validateDate(inputValue,view.getDateFormat())) {
            result = false;
            newErrorMsg += VALIDATION_MSG_DATE;
        }
        if(!StringUtil.isEmpty(newErrorMsg)) {
            errorMsg += StringUtil.isEmpty(errorMsg) ? newErrorMsg : "\n"+newErrorMsg;
            view.setError(errorMsg);
        }
        return result;
    }

    /**
     * perform validation by specified dynamic array of Abstract widget
     * @param views
     * @return number of failed validation
     */
    public static int validate (AbstractWidget... views) {
        int numOfFailed = 0;
        for(AbstractWidget view : views) {
            view.setError(null);
            for(String validator : view.getValidators()) {
                boolean valid = validate(validator,view);
                if(!valid) {
                    numOfFailed++;
                }
            }
        }

        return numOfFailed;
    }

    /**
     * Validate single component
     * @param view
     * @return
     */
    public static Boolean validateSingleComponent(AbstractWidget view) {
        String inputValue = view.getInputValue().toString();
        int numOfFailed = validate(view);
        return numOfFailed == 0 ? true : false;
    }

    /**
     * perform validation by find the AbstractWidget in child of the specified layout
     * @param v
     * @return true if validation success, otherwise false
     */
    public static Boolean validateGroup (Context context,ViewGroup v) {
        int numOfFailedTotal = 0;
            // Inside some layout with child
        for (int i = 0; i < v.getChildCount(); i++) {
            Object child = v.getChildAt(i);
            if (child instanceof AbstractWidget) {
                AbstractWidget abstractWidget = (AbstractWidget) child;
               int numOfFailed = validate(abstractWidget);
               numOfFailedTotal += numOfFailed;
            }
        }

        if(numOfFailedTotal > 0) {
            return false;
        }
        return true;
    }

}
