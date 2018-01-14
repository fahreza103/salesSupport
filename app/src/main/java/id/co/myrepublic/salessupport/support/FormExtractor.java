package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.HashMap;

import id.co.myrepublic.salessupport.widget.CustomEditText;

/**
 * Used to extract values for input form
 */

public class FormExtractor {

    /**
     * Extract all values from input form inside ViewGroup child
     * @param context
     * @param v
     * @return HashMap with key from id component and it's value
     */
    public static HashMap<String,Object> extractValues(Context context, ViewGroup v) {
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        for(int i=0;i<v.getChildCount();i++) {
            Object child = v.getChildAt(i);
            if (child instanceof CustomEditText) {
                CustomEditText editText = (CustomEditText)child;
                resultMap.put(context.getResources().getResourceEntryName(editText.getId()),editText.getInputValue());
            } else if(child instanceof Spinner) {
                Spinner spinner = (Spinner) child;
                resultMap.put(context.getResources().getResourceEntryName(spinner.getId()), spinner.getSelectedItem().toString());
            } else if(child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                resultMap.put(context.getResources().getResourceEntryName(checkBox.getId()), checkBox.isChecked());
            } else if(child instanceof LinearLayout) {
                // Another layout viewGroup with a child, do recursive call
                LinearLayout linearLayout = (LinearLayout) child;
                extractValues(context,linearLayout);
            } else if(child instanceof RelativeLayout) {
                // Another layout viewGroup with a child, do recursive call
                RelativeLayout relativeLayout = (RelativeLayout) child;
                extractValues(context,relativeLayout);
            }
        }
        return resultMap;
    }
}
