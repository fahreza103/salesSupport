package id.co.myrepublic.salessupport.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;

/**
 * Common Row Adapter to populate custom listview with 2 maintext on top and 2 subtext below the maintext
 * @param <T> any model class to populate data, make sure inside the model resides PositionItem Annotation on the fields otherwise no item will shown in the list
 *
 * @author Fahreza Tamara
 */
public class CommonRowAdapter<T> extends ArrayAdapter<T> {

    private List<String> dataSet;
    private Context mContext;
    private Integer widthText;
    private Integer rowHeight;
    private Integer textGravity;
    private CommonRowAdapter.ViewHolder viewHolder;
    private Boolean isSpinner = false;
    private Boolean isProgress = false;
    private Boolean isMapData  = false;


    // View lookup cache
    private static class ViewHolder {
        private RelativeLayout rowLayout;
        private TextView mainText1;
        private TextView mainText2;
        private TextView subText1;
        private TextView subText2;
        private ProgressBar progressBar;
    }

    public CommonRowAdapter(List<T> data, Context context, int textViewResourceId) {
        super(context, R.layout.row_item_common,textViewResourceId, data);
        this.dataSet = Arrays.asList(new String[data.size()]);
        this.mContext=context;
    }

    public CommonRowAdapter(List<T> data, Context context) {
        super(context, R.layout.row_item_common, data);
        this.dataSet = Arrays.asList(new String[data.size()]);
        this.mContext=context;
    }

    public CommonRowAdapter(Map<Object,Object> dataMap, Context context) {
        // set map values as adapter list item value
        super(context, R.layout.row_item_common, (List<T>) Arrays.asList(dataMap.values().toArray()));
        // set map key as view item
        this.dataSet =  Arrays.asList(dataMap.keySet().toArray(new String[dataMap.size()]));
        this.isMapData = true;
    }

    private int lastPosition = -1;

    // For spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getComponentView(position,convertView,parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getComponentView(position,convertView,parent);
    }


    public View getComponentView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;

        if (convertView == null) {

            viewHolder = new CommonRowAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_common, parent, false);

            viewHolder.rowLayout = (RelativeLayout) convertView.findViewById(R.id.rowitem_layout);
            viewHolder.mainText1 = (TextView) convertView.findViewById(R.id.rowitem_maintext);
            viewHolder.mainText2= (TextView) convertView.findViewById(R.id.rowitem_maintext2);
            viewHolder.subText1 = (TextView) convertView.findViewById(R.id.rowitem_subtext);
            viewHolder.subText2 = (TextView) convertView.findViewById(R.id.rowitem_subtext2);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.rowitem_progressbar);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonRowAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }


        boolean hasSubText = false;

        if(dataModel instanceof String) {
            // if map is supplied in constructor
            String value = "";
            if(isMapData) {
                value = dataSet.get(position);
            } else {
                value = (String) dataModel;
            }
            viewHolder.mainText1.setText(!StringUtil.isEmpty(value) ? value : "");
            
        } else {
            // Read the annotation
            try {
                for (Field field : dataModel.getClass().getDeclaredFields()) {
                    Annotation annotation = field.getAnnotation(PositionItem.class);
                    field.setAccessible(true);

                    String fieldValue = getStringFromReflectionObject(field.get(dataModel));
                    if (annotation instanceof PositionItem) {
                        PositionItem posItem = (PositionItem) annotation;
                        RowItem annotationValue = posItem.type();
                        String prefix = posItem.prefix();
                        String postfix = posItem.postfix();

                        if (RowItem.MAINTEXT1 == annotationValue) {
                            viewHolder.mainText1.setText(!StringUtil.isEmpty(fieldValue) ? prefix + fieldValue + postfix : "");
                            dataSet.set(position,viewHolder.mainText1.getText().toString());
                        }
                        if (RowItem.MAINTEXT2 == annotationValue) {
                            viewHolder.mainText2.setText(!StringUtil.isEmpty(fieldValue) ? prefix + fieldValue + postfix : "");
                        }
                        if (RowItem.SUBTEXT1 == annotationValue) {
                            hasSubText = true;
                            viewHolder.subText1.setText(fieldValue != null && !"".equals(fieldValue) ? prefix + fieldValue + postfix : "");
                        }
                        if (RowItem.SUBTEXT2 == annotationValue) {
                            hasSubText = true;
                            viewHolder.subText2.setText(fieldValue != null && !"".equals(fieldValue) ? prefix + fieldValue + postfix : "");
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        // Make gone so it not using any space
        if(!hasSubText) {
            viewHolder.subText1.setVisibility(View.GONE);
            viewHolder.subText2.setVisibility(View.GONE);
        }

        // Set width beetween Text
        if(widthText != null) {
            int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthText, mContext.getResources().getDisplayMetrics());
            viewHolder.mainText1.getLayoutParams().width=dimensionInDp;
            viewHolder.mainText1.requestLayout();

            viewHolder.subText1.getLayoutParams().width=dimensionInDp;
            viewHolder.subText1.requestLayout();
        }

        // Layout height
        if(rowHeight != null) {
            int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowHeight, mContext.getResources().getDisplayMetrics());
            viewHolder.rowLayout.getLayoutParams().height=dimensionInDp;
            viewHolder.rowLayout.requestLayout();
        }

        // Text Gravity
        if(textGravity != null) {
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)viewHolder.mainText1.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        }

        // Spinner animation only animate the text, not the entire layout because it will stacked when changing item selected
        GlobalVariables gvar = GlobalVariables.getInstance();
        if(gvar.getTopDownAnim() != null) {
            if (isSpinner) {
                viewHolder.mainText1.startAnimation((position > lastPosition) ? gvar.getTopDownAnim() : gvar.getDownTopAnim());
            } else {
                result.startAnimation((position > lastPosition) ? gvar.getTopDownAnim() : gvar.getDownTopAnim());
            }
        }

        // Show progressbar
        if(isProgress) {
            viewHolder.progressBar.setVisibility(View.VISIBLE);
        }

        lastPosition = position;
        // Return the completed view to render on screen
        return convertView;
    }

    private String getStringFromReflectionObject(Object obj) {
        if(obj != null) {
            if (obj instanceof String) {
                return (String) obj;
            } else if (obj instanceof Integer) {
                return Integer.toString((Integer)obj);
            } else if (obj instanceof Date) {
                return new SimpleDateFormat("MM/dd/yyyy").format((Date) obj);
            }
        }
        return null;
    }

    public Integer getWidthText() {
        return widthText;
    }

    /**
     * Set width in dp
     * @param widthText
     */
    public void setWidthText(Integer widthText) {
        this.widthText = widthText;
    }

    public Integer getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
    }

    public Integer getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(Integer textGravity) {
        this.textGravity = textGravity;
    }

    public Boolean getSpinner() {
        return isSpinner;
    }

    public void setSpinner(Boolean spinner) {
        isSpinner = spinner;
    }

    public List<String> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    public ProgressBar getProgressBar() {
        return viewHolder.progressBar;
    }

    public Boolean getProgress() {
        return isProgress;
    }

    public void setProgress(Boolean progress) {
        isProgress = progress;
    }
}
