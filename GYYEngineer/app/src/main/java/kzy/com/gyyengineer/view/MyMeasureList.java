package kzy.com.gyyengineer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 创建日期：2017/7/11 0011 on 10:23
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class MyMeasureList extends ListView {
    public MyMeasureList(Context context) {
        super(context);
    }

    public MyMeasureList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMeasureList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, i);
    }
}
