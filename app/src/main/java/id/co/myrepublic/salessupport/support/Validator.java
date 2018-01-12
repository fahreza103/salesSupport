package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.widget.CustomEditText;



/**
 * Class to define all validation implemetation
 */

public class Validator {

    public static final String VALIDATION_REQUIRED = "required";
    public static final String VALIDATION_EMAIL = "email";
    public static final String VALIDATION_DATE = "date";

    public static final String VALIDATION_MSG_REQUIRED = "This field is required";
    public static final String VALIDATION_MSG_EMAIL = "Invalid email format";
    public static final String VALIDATION_MSG_DATE = "Invalid date format";

    private static final String DEFAULT_DATE_FORMAT = "mm/DD/yyyy";

    private static Boolean validateRequired(String value) {
        return !StringUtil.isEmpty(value);
    }

    private static Boolean validateEmail(String value) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private static Boolean validateDate(String value, String format) {
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
     * perform validation
     * @param editText
     * @return
     */
    private static Boolean validate (Boolean result, String validator, CustomEditText editText) {
        String errorMsg = editText.getError()== null ? "": editText.getError();
        String newErrorMsg = "";
        if(validator.equals(VALIDATION_REQUIRED) && !validateRequired(editText.getInputText())) {
            result = false;
            newErrorMsg += VALIDATION_MSG_REQUIRED;
        } else if(validator.equals(VALIDATION_EMAIL) && !validateEmail(editText.getInputText())) {
            result = false;
            newErrorMsg += VALIDATION_MSG_EMAIL;
        } else if(validator.equals(VALIDATION_DATE) && !validateDate(editText.getInputText(),editText.getDateFormat())) {
            result = false;
            newErrorMsg += VALIDATION_MSG_DATE;
        }
        if(!StringUtil.isEmpty(newErrorMsg)) {
            errorMsg += StringUtil.isEmpty(errorMsg) ? newErrorMsg : "\n"+newErrorMsg;
            editText.setError(errorMsg);
        }
        return result;
    }

    /**
     * perform validation by specified array of CustomEditText
     * @param editTexts
     * @return true if validation success, otherwise false
     */
    public static Boolean validate (CustomEditText... editTexts) {
        boolean result = true;
        for(CustomEditText editText : editTexts) {
            editText.setError(null);
            for(String validator : editText.getValidators()) {
                result = validate(result,validator,editText);
            }
        }
        return result;
    }

    /**
     * perform validation by find the CustomEditText in child of the specified layout
     * @param v
     * @return true if validation success, otherwise false
     */
    public static Boolean validate (ViewGroup v, Context context) {
        boolean result = true;
        for(int i=0;i<v.getChildCount();i++) {
            Object child = v.getChildAt(i);
            if (child instanceof CustomEditText) {
                CustomEditText editText = (CustomEditText)child;
                editText.setError(null);
                for(String validator : editText.getValidators()) {
                    result = validate(result,validator,editText);
                }
            }
        }
        if(context != null && !result) {
            Toast.makeText(context, "Validation error, please check your fields",
                    Toast.LENGTH_LONG).show();
        }
        return result;
    }

}
