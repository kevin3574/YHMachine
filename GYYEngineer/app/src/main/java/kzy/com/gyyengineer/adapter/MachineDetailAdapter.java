package kzy.com.gyyengineer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.model.CurrentDeteilBean;

/**
 * 创建日期：2017/6/28 0028 on 9:49
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class MachineDetailAdapter extends BaseAdapter {
    private final CurrentDeteilBean currentDeteilBean;
    private final Context context;
    private ViewHoLder viewHoLder;

    public MachineDetailAdapter(Context context, CurrentDeteilBean currentDeteilBean) {
        this.currentDeteilBean = currentDeteilBean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return currentDeteilBean == null ? 0 : currentDeteilBean.getRecordData().size();

    }

    @Override
    public CurrentDeteilBean.RecordDataBean getItem(int position) {
        return currentDeteilBean.getRecordData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.machine_detail_lv_item_layout, null);
            viewHoLder = new ViewHoLder();
            viewHoLder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHoLder.tv_wxxq = (TextView) convertView.findViewById(R.id.tv_wxxq);
            convertView.setTag(viewHoLder);
        } else {
            viewHoLder = (ViewHoLder) convertView.getTag();
        }
        CurrentDeteilBean.RecordDataBean recordDataBean = currentDeteilBean.getRecordData().get(position);
        viewHoLder.tv_time.setText(recordDataBean.getRecordTime());
        viewHoLder.tv_wxxq.setText(recordDataBean.getRepairDetail());
        return convertView;
    }

    public class ViewHoLder {
        public TextView tv_time;
        public TextView tv_wxxq;
    }
}
