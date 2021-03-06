package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;

/**
 * Collection of checkbox, and with labelText
 *
 * @author Fahreza Tamara
 */

public class Checkboxes extends AbstractWidget {

    private List<CheckBox> checkboxes;
    private LinearLayout checkboxParentLayout;
    private ProgressBar progressBar;
    private TextView textStatus;
    private Map<String,Object> mapValues ;

    public Checkboxes(Context context) {
        super(context);
    }

    public Checkboxes(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.customview_common, R.id.custom_layout);
    }

    @Override
    protected void initCustomAttr(Context context, AttributeSet attrs) {
        this.checkboxParentLayout = (LinearLayout) this.widgetView;
        this.progressBar = (ProgressBar) findViewById(R.id.custom_progressbar);
        this.textStatus = (TextView) findViewById(R.id.custom_txt_status);
        this.checkboxes = new ArrayList<CheckBox>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.appAttr);

        CharSequence[] entries = typedArray.getTextArray(R.styleable.appAttr_android_entries);
        int selectedItem = typedArray.getInteger(R.styleable.appAttr_selectedItem,0);
        typedArray.recycle();
        setEntries(entries);
    }

    /**
     * show progressbar while waiting data has been populated
     */
    public void runProgress() {
        this.textStatus.setVisibility(VISIBLE);
        this.textStatus.setText(LOADING_TEXT);
        this.progressBar.setVisibility(VISIBLE);
    }

    private void removeCheckboxInLayout() {
        if(checkboxParentLayout != null) {
            for(int i=0;i<checkboxParentLayout.getChildCount();i++) {
                Object child = checkboxParentLayout.getChildAt(i);
                if(child instanceof CheckBox) {
                    checkboxParentLayout.removeView((View) child);
                }
            }
            this.checkboxes.clear();
        }
    }

    /**
     * Set checkbox entries with map values
     * @param mapValues
     */
    public void setEntriesMap(Map<String,Object> mapValues) {
        this.progressBar.setVisibility(GONE);
        // Remove all checkbox view from previous
        removeCheckboxInLayout();
        if(mapValues != null && mapValues.size() > 0) {
            this.textStatus.setVisibility(GONE);
            this.mapValues = mapValues;
            for (Map.Entry<String, Object> entry : mapValues.entrySet()) {
                addCheckbox(entry.getKey());
            }
        } else {
            this.textStatus.setText(ITEM_EMPTY);
        }

    }

    /**
     * Add checboxes based on entries, in tag xml
     * @param entries
     */
    public void setEntries(CharSequence[] entries) {
        this.progressBar.setVisibility(GONE);
        removeCheckboxInLayout();
        if(entries != null && entries.length >0) {
            this.textStatus.setVisibility(GONE);
            List<String> dataList = new ArrayList<String>();
            for (CharSequence entry : entries) {
                addCheckbox(entry.toString());
            }
        } else {
            this.textStatus.setText(ITEM_EMPTY);
        }
    }

    /**
     * get single checkbox by tag
     * @param tagName
     * @return
     */
    public CheckBox getCheckboxByTag(String tagName) {
        if(!tagName.contains(CHECKBOX_TAG)) {
            tagName = CHECKBOX_TAG+"_"+tagName;
        }
        return (CheckBox) findViewWithTag(tagName);
    }

    /**
     * Get multiple checbox by tag array
     * @param tagNameList
     * @return
     */
    public Map<String,CheckBox> getCheckboxByTag(String... tagNameList) {
        Map<String,CheckBox> result = new HashMap<String,CheckBox>();
        for(String tagName : tagNameList) {
            CheckBox checkBox = getCheckboxByTag(tagName);
            result.put(tagName,checkBox);
        }
        return result;
    }

    /**
     * Add checkbox on the view
     * @param entry
     * @return
     */
    public CheckBox addCheckbox(String entry) {
        this.progressBar.setVisibility(GONE);
        this.textStatus.setVisibility(GONE);
        CheckBox checkBox = new CheckBox(this.context);
        checkBox.setText(entry);
        checkBox.setTag(CHECKBOX_TAG+"_"+entry);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        checkboxes.add(checkBox);
        checkboxParentLayout.addView(checkBox);
        return checkBox;
    }

    /**
     * Set checked state of all the checkbox
     * @param checked
     */
    public void setChecked(Boolean checked) {
        setChecked(checked, checkboxes.toArray(new CheckBox[checkboxes.size()]));
    }

    /**
     * Set specified checked state by tag names
     * @param checked
     * @param tagNames
     */
    public void setChecked(Boolean checked, String... tagNames) {
        setChecked(checked,  getCheckboxByTag(tagNames).values().toArray(new CheckBox[tagNames.length]));
    }

    /**
     * toggle checked state on the checkbox
     * @param checked
     * @param checkBoxes
     */
    private void setChecked(Boolean checked,CheckBox... checkBoxes) {
        for(CheckBox checkBox : checkBoxes) {
            if(checked) {
                if(!checkBox.isChecked()) {
                    checkBox.toggle();
                }
            } else {
                if(checkBox.isChecked()) {
                    checkBox.toggle();
                }
            }
        }
    }

    /**
     * Set enabled for all the checkbox
     * @param checked
     */
    public void setCheckEnabled(Boolean checked) {
        setCheckEnabled(checked,  checkboxes.toArray(new CheckBox[checkboxes.size()]));
    }

    /**
     * Set specified enabled by the tagName of the checkbox
     * @param enabled
     * @param tagNames
     */
    public void setCheckEnabled(Boolean enabled, String... tagNames) {
        setCheckEnabled(enabled, (CheckBox[]) getCheckboxByTag(tagNames).values().toArray(new CheckBox[tagNames.length]));
    }

    /**
     * set the enabled state
     * @param enabled
     * @param checkBoxes
     */
    public void setCheckEnabled(Boolean enabled,CheckBox... checkBoxes) {
        for(CheckBox checkBox : checkBoxes) {
            checkBox.setEnabled(enabled);
        }
    }

    @Override
    public void setError(String message) {}

    @Override
    public void setError(String message, Drawable drawable) {}

    @Override
    public String getError() {
        return null;
    }

    @Override
    public Object getInputValue() {
        Map<String,CheckboxParam> result = new HashMap<String,CheckboxParam>();
        for(CheckBox checkBox : checkboxes) {
            CheckboxParam checkboxParam = new CheckboxParam();
            checkboxParam.setChecked(checkBox.isChecked());
            if(mapValues != null)
                checkboxParam.setValue(mapValues.get(checkBox.getText()));
            result.put(checkBox.getTag().toString(), checkboxParam);
        }
        return result;
    }

    public List<CheckBox> getCheckboxes() {
        return checkboxes;
    }

    public void setCheckboxes(List<CheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    /**
     *  This is to save the current state of this component after going into the next fragment / activity
     *
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());

        HashMap<String,CheckboxParam> result = (HashMap<String, CheckboxParam>) getInputValue();
        bundle.putSerializable("checkBoxSelected", result);
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

            HashMap<String,CheckboxParam> resultMap = (HashMap<String, CheckboxParam>) bundle.getSerializable("checkBoxSelected");
            for (Map.Entry<String, CheckboxParam> entry : resultMap.entrySet()) {
                CheckboxParam checkboxParam = entry.getValue();
                setChecked(checkboxParam.getChecked(),entry.getKey());
            }
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

}
