package kzy.com.gyyengineer.adapter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.model.CurrentDataBean;

/**
 * 创建日期：2017/6/21 0021 on 9:09
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class HomeCurrentLvAdapter extends BaseAdapter {
    private final FragmentActivity activity;
    private final CurrentDataBean currentDataBean;

    public HomeCurrentLvAdapter(FragmentActivity activity, CurrentDataBean currentDataBean) {
        this.activity = activity;
        this.currentDataBean = currentDataBean;
    }

    @Override
    public int getCount() {
        return currentDataBean == null ? 0 : currentDataBean.getData().size();
    }

    @Override
    public CurrentDataBean.DataBean getItem(int position) {

        return currentDataBean.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder viewholder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.home_current_lv_item, null);
            viewholder = new viewholder();
            viewholder.tv_ddh = (TextView) convertView.findViewById(R.id.tv_ddh);
            viewholder.tv_zt = (TextView) convertView.findViewById(R.id.tv_zt);
            viewholder.tv_jqpp = (TextView) convertView.findViewById(R.id.tv_jqpp);
            viewholder.tv_jqlx = (TextView) convertView.findViewById(R.id.tv_jqlx);
            viewholder.tv_bxqy = (TextView) convertView.findViewById(R.id.tv_bxqy);
            viewholder.tv_gzms = (TextView) convertView.findViewById(R.id.tv_gzms);
            viewholder.tv_bxsj = (TextView) convertView.findViewById(R.id.tv_bxsj);
            convertView.setTag(viewholder);
        } else {
            viewholder = (HomeCurrentLvAdapter.viewholder) convertView.getTag();
        }
        CurrentDataBean.DataBean dataBean = getItem(position);
        Log.e("HomeCurrentLvAdapter", "--6.1接口数据--" + dataBean);
        viewholder.tv_ddh.setText(dataBean.getCloudOrder());
        if ("1".equals(dataBean.getOrderStatus())) {
            viewholder.tv_zt.setText("已接单");
        } else {
            viewholder.tv_zt.setText("等待接单");
        }
        viewholder.tv_jqpp.setText(dataBean.getMachineBrand());
        viewholder.tv_jqlx.setText(dataBean.getMarchineType());
        viewholder.tv_bxqy.setText(dataBean.getCloudAddress());
        viewholder.tv_gzms.setText(dataBean.getFaultDescription());
        viewholder.tv_bxsj.setText(dataBean.getRepairTime());
        return convertView;
    }

    //long转String  时间格式
    private String long2data(long repairTime, String s) {
        Date date = new Date();
        String format = new SimpleDateFormat(s).format(date);
        return format;
    }


    public class viewholder {
        public TextView tv_ddh;
        public TextView tv_zt;
        public TextView tv_jqpp;
        public TextView tv_jqlx;
        public TextView tv_bxqy;
        public TextView tv_gzms;
        public TextView tv_bxsj;
    }
}
