package id.co.myrepublic.salessupport.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by myrepublic on 1/19/18.
 */

public abstract class AbstractWidget extends LinearLayout {

    protected Context context;

    public AbstractWidget(Context context) {
        super(context);
        this.context = context;
    }

    public AbstractWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initBaseAttr(context, attrs);
        initCustomAttr(context,attrs);
        
    }

    private void initBaseAttr(Context context, AttributeSet attrs) {

    }


    public abstract void initCustomAttr(Context context, AttributeSet attrs);


}
