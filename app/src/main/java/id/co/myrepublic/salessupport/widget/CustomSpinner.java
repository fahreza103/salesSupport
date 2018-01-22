package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
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

        spinner.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    setError(null);
            }
        });

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

    public void setSelectedItem(Integer index) {
        spinner.setSelection(index);
    }

    public Object getSelectedItem() {
        return spinner.getSelectedItem();
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

    public String getInputTextValue() {
        String value = null;
        int spinnerSelectedIndex = spinner.getSelectedItemPosition();
        List<String> dataSet = ((CommonRowAdapter) adapter).getDataSet();
        return dataSet.get(spinnerSelectedIndex);
    }


}
