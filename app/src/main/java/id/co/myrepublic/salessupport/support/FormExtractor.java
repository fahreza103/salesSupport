package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

import id.co.myrepublic.salessupport.widget.AbstractWidget;

/**
 * Used to extract values for input form
 *
 * @author Fahreza Tamara
 */

public class FormExtractor {


    /**
     * Extract all values from input form inside ViewGroup child
     * @param context
     * @param v
     * @param validate extract and validate
     * @return HashMap with key from id component and it's value
     */
    public static HashMap<String,Object> extractValues(Context context, ViewGroup v, Boolean validate) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        AbstractWidget focusInvalidWidget = null;
        // default should be true
        resultMap.put(Validator.VALIDATION_KEY_RESULT, true);
        int numOfFailed = 0;
        for(int i=0;i<v.getChildCount();i++) {
            Object child = v.getChildAt(i);
            if (child instanceof AbstractWidget) {
                AbstractWidget abstractWidget = (AbstractWidget)child;
                resultMap.put(context.getResources().getResourceEntryName(abstractWidget.getId()),abstractWidget.getInputValue());

                if(validate) {
                    int failed = Validator.validate(abstractWidget);
                    if(failed > 0) {
                        numOfFailed+= failed;
                        focusInvalidWidget = abstractWidget;
                    }
                }
            } else if(child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                resultMap.put(context.getResources().getResourceEntryName(checkBox.getId()), checkBox.isChecked());
            } else if(child instanceof LinearLayout) {
                // Another layout viewGroup with a child, do recursive call
                LinearLayout linearLayout = (LinearLayout) child;
                resultMap.putAll(extractValues(context,linearLayout,validate));
            } else if(child instanceof RelativeLayout) {
                // Another layout viewGroup with a child, do recursive call
                RelativeLayout relativeLayout = (RelativeLayout) child;
                resultMap.putAll(extractValues(context,relativeLayout,validate));
            }
        }

        if(numOfFailed > 0) {
            resultMap.put(Validator.VALIDATION_KEY_RESULT, false);
            Toast.makeText(context, Validator.VALIDATION_MSG_FAILED,
                    Toast.LENGTH_SHORT).show();
            focusInvalidWidget.requestFocus();
        }
        return resultMap;
    }
}
