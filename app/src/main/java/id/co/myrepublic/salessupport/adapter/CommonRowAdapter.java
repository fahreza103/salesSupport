package id.co.myrepublic.salessupport.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.AppConstant;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;

/**
 * Common Row Adapter to populate custom listview with 2 maintext on top and 2 subtext below the maintext
 * @param <T> any model class to populate data, make sure inside the model resides PositionItem Annotation on the fields otherwise no item will shown in the list
 */
public class CommonRowAdapter<T> extends ArrayAdapter<T> {

    private List<T> dataSet;
    private Context mContext;
    private Integer widthText;

    // View lookup cache
    private static class ViewHolder {
        TextView mainText1;
        TextView mainText2;
        TextView subText1;
        TextView subText2;
    }

    public CommonRowAdapter(List<T> data, Context context) {
        super(context, R.layout.row_item_common, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CommonRowAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CommonRowAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_common, parent, false);

            viewHolder.mainText1 = (TextView) convertView.findViewById(R.id.rowitem_maintext);
            viewHolder.mainText2= (TextView) convertView.findViewById(R.id.rowitem_maintext2);
            viewHolder.subText1 = (TextView) convertView.findViewById(R.id.rowitem_subtext);
            viewHolder.subText2 = (TextView) convertView.findViewById(R.id.rowitem_subtext2);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonRowAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        GlobalVariables gvar = GlobalVariables.getInstance();
        result.startAnimation((position > lastPosition) ? gvar.getTopDownAnim() : gvar.getDownTopAnim());
        lastPosition = position;
        boolean hasSubText = false;

        // Read the annotation
        try {
            for (Field field : dataModel.getClass().getDeclaredFields()) {
                Annotation annotation = field.getAnnotation(PositionItem.class);
                field.setAccessible(true);

                String fieldValue = getStringFromReflectionObject(field.get(dataModel));
                if(annotation instanceof  PositionItem) {
                    PositionItem posItem = (PositionItem) annotation;
                    String annotationValue = posItem.type();
                    String prefix = posItem.prefix();
                    String postfix = posItem.postfix();

                    if(AppConstant.ROWITEM_POSITION_MAINTEXT1.equals(annotationValue)) {
                        viewHolder.mainText1.setText(!StringUtil.isEmpty(fieldValue) ? prefix+fieldValue+postfix : "");
                    }
                    if(AppConstant.ROWITEM_POSITION_MAINTEXT2.equals(annotationValue)) {
                        viewHolder.mainText2.setText(!StringUtil.isEmpty(fieldValue) ? prefix+fieldValue+postfix : "");
                    }
                    if(AppConstant.ROWITEM_POSITION_SUBTEXT1.equals(annotationValue)) {
                        hasSubText = true;
                        viewHolder.subText1.setText(fieldValue != null && !"".equals(fieldValue) ? prefix+fieldValue+postfix : "");
                    }
                    if(AppConstant.ROWITEM_POSITION_SUBTEXT2.equals(annotationValue)) {
                        hasSubText = true;
                        viewHolder.subText2.setText(fieldValue != null && !"".equals(fieldValue) ? prefix+fieldValue+postfix : "");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
