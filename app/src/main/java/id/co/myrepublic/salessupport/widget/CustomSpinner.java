package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.adapter.CommonRowAdapter;
import id.co.myrepublic.salessupport.util.StringUtil;


/**
 * Custom spinner with label Text and validation implementation
 *
 * @author Fahreza Tamara
 */

public class CustomSpinner extends AbstractWidget {

    private Spinner spinner;
    private ArrayAdapter adapter;
    private TextView errorTextView;

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs,R.layout.customview_spinner,R.id.custom_spinner_box);
    }


    @Override
    protected void initCustomAttr(Context context, AttributeSet attrs) {
        spinner = (Spinner) this.widgetView;
        errorTextView = (TextView) findViewById(R.id.custom_widget_error_text);
        errorTextView.setVisibility(GONE);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.appAttr);

        CharSequence[] entries = typedArray.getTextArray(R.styleable.appAttr_android_entries);
        int selectedItem = typedArray.getInteger(R.styleable.appAttr_selectedItem,0);
        typedArray.recycle();
        setEntries(entries);

    }

    public void setEntries(CharSequence[] entries) {
        if(entries != null && entries.length >0) {
            List<String> dataList = new ArrayList<String>();
            for(CharSequence entry : entries) {
                dataList.add(entry != null ? entry.toString():"");
            }
            CommonRowAdapter<String> adapter = new CommonRowAdapter<String>(dataList,this.context);
            adapter.setSpinner(true);
            setAdapter(adapter);
        }
    }

    public void setAdapter(ArrayAdapter adapter) {
        spinner.setAdapter(adapter);
        this.adapter = adapter;
    }

    public void setSelectedIndex(Integer index) {
        spinner.setSelection(index);
    }

    public Object getSelectedItem() {
        return spinner.getSelectedItem();
    }

    public int getSelectedIndex() {
        return spinner.getSelectedItemPosition();
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener){
        spinner.setOnItemSelectedListener(listener);
    }


    @Override
    public void setError(String message) {
        setError(message,null);
    }

    @Override
    public void setError(String message, Drawable drawable) {
        if(StringUtil.isEmpty(message)) {
            errorTextView.setText("");
            errorTextView.setVisibility(GONE);
        } else {
            errorTextView.setText(message);
            errorTextView.setVisibility(VISIBLE);
        }
        this.errorMsg = message;
    }

    @Override
    public String getError() {
        return errorMsg;
    }

    @Override
    public Object getInputValue() {
        return spinner.getSelectedItem();
    }

    /**
     * Get text inside spinner selected item
     * @return String text
     */
    public String getInputTextValue() {
        String value = null;
        int spinnerSelectedIndex = getSelectedIndex();
        List<String> dataSet = ((CommonRowAdapter) adapter).getDataSet();
        return dataSet.get(spinnerSelectedIndex);
    }

    /**
     * Show loading view item for the spinner while waiting fetching data,
     * please note, this will eliminate the items inside the spinner
     */
    public void runProgress() {
        List<String> loadingItem = new ArrayList<String>();
        loadingItem.add(AbstractWidget.LOADING_TEXT);

        CommonRowAdapter<String> adapter = new CommonRowAdapter<String>(loadingItem,this.context);
        adapter.setSpinner(true);
        adapter.setProgress(true);
        setAdapter(adapter);

    }


    /**
     *  This is to save the current state of this component after going into the next fragment / activity
     *
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("selectedIndex",getSelectedIndex());
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
            int selectedIndex = bundle.getInt("selectedIndex");
            setSelectedIndex(selectedIndex);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }


}
