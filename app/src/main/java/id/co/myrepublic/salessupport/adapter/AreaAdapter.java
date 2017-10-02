package id.co.myrepublic.salessupport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.model.Area;

/**
 * Created by myrepublicid on 26/9/17.
 */

public class AreaAdapter extends ArrayAdapter<Area> {

    private List<Area> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView mainText;
        TextView subText1;
        TextView subText2;
    }

    public AreaAdapter(List<Area> data, Context context) {
        super(context, R.layout.row_item_datacity, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Area dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_datacity, parent, false);
            viewHolder.mainText = (TextView) convertView.findViewById(R.id.cityname);
            viewHolder.subText1 = (TextView) convertView.findViewById(R.id.arpu);
            viewHolder.subText2 = (TextView) convertView.findViewById(R.id.active_subs);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.mainText.setText(dataModel.getAreaName());
        viewHolder.subText1.setText(dataModel.getAreaCode());
        //viewHolder.subText2.setText(dataModel.getAreaCode());

        // Return the completed view to render on screen
        return convertView;
    }
}