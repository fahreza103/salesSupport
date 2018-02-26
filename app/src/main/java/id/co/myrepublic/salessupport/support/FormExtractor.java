package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.widget.AbstractWidget;
import id.co.myrepublic.salessupport.widget.CheckboxParam;
import id.co.myrepublic.salessupport.widget.Checkboxes;
import id.co.myrepublic.salessupport.widget.CustomEditText;
import id.co.myrepublic.salessupport.widget.CustomSpinner;

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

    /**
     * Put value into form based on id
     * @param viewGroup
     * @return HashMap with key from id component and it's value
     */
    public static void putValues(Map<String,Object> values, ViewGroup viewGroup) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            int id = getId(entry.getKey(), R.id.class);
            if(id>0) {
                View v = viewGroup.findViewById(id);
                if(v instanceof AbstractWidget) {
                    setFormValue(v,entry.getValue());
                }
            } else {
                Log.w("Resource ID","ID not found with name "+entry.getKey());
            }
        }
    }

    private static void setFormValue(View v, Object value) {
        AbstractWidget abstractWidget = (AbstractWidget) v;
        if(abstractWidget instanceof CustomEditText) {
            ((CustomEditText) abstractWidget).setInputValue(value+"");
        } else if(abstractWidget instanceof CustomSpinner) {
            CustomSpinner customSpinner = (CustomSpinner) abstractWidget;
            if(customSpinner.getAdapter() != null) {
                int index = customSpinner.getAdapter().getPosition(value);
                customSpinner.setSelectedIndex(index);
            }
        } else if(abstractWidget instanceof Checkboxes) {
            Checkboxes checkboxes = (Checkboxes) v;
            Map<String,CheckboxParam> checkboxMap = (Map<String,CheckboxParam> ) value;
            for (Map.Entry<String, CheckboxParam> entry : checkboxMap.entrySet()) {
                CheckboxParam checkBoxParam = entry.getValue();
                checkboxes.setCheckEnabled(checkBoxParam.getChecked(),entry.getKey());
            }

        }
    }

    /**
     * Get int ID by String resourceName
     * @param resourceName
     * @param c
     * @return
     */
    private static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
