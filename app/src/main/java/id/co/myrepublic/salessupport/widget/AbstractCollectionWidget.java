//package id.co.myrepublic.salessupport.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.widget.CheckBox;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import id.co.myrepublic.salessupport.R;
//
///**
// * Abstract implementation of collection widget, handling multiple widget in single group view
// *
// * @author Fahreza Tamara
// */
//
//public abstract class AbstractCollectionWidget extends  AbstractWidget {
//
//    protected final String WIDGET_TYPE_CHECKBOX = "widgetCheckbox";
//    protected final String WIDGET_TYPE_EDITTEXT = "widgetCheckbox";
//    protected final String WIDGET_TYPE_SPINNER = "widgetCheckbox";
//
//    private LinearLayout checkboxParentLayout;
//
//
//    public AbstractCollectionWidget(Context context) {
//        super(context);
//    }
//
//    public AbstractCollectionWidget(Context context, AttributeSet attrs, Integer layout) {
//        super(context, attrs, layout);
//    }
//
//    public AbstractCollectionWidget(Context context, AttributeSet attrs, Integer layout, Integer inputId) {
//        super(context, attrs, layout, inputId);
//    }
//
//    @Override
//    protected void initCustomAttr(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.appAttr);
//
//        CharSequence[] entries = typedArray.getTextArray(R.styleable.appAttr_android_entries);
//        int selectedItem = typedArray.getInteger(R.styleable.appAttr_selectedItem,0);
//        typedArray.recycle();
//        setEntries(entries);
//    }
//
//    public void setEntries(CharSequence[] entries) {
//        checkboxParentLayout = (LinearLayout) this.widgetView;
//        if(entries != null && entries.length >0) {
//            List<String> dataList = new ArrayList<String>();
//
//            for (CharSequence entry : entries) {
//                CheckBox checkBox = new CheckBox(this.context);
//                checkBox.setText(entry);
//                checkBox.setTag(CHECKBOX_TAG+"_"+entry);
//                checkBox.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT,
//                        LayoutParams.WRAP_CONTENT));
//                checkboxes.add(checkBox);
//                checkboxParentLayout.addView(checkBox);
//
//            }
//        }
//    }
//
//    @Override
//    public void setError(String message) {
//        // Not yet implemented
//    }
//
//    @Override
//    public void setError(String message, Drawable drawable) {
//        // Not yet implemented
//    }
//
//    @Override
//    public String getError() {
//        return null;
//    }
//
//
//}
