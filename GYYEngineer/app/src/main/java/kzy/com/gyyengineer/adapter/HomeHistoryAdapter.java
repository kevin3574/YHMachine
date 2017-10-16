package kzy.com.gyyengineer.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.engineer.activity.SeeEvaluateActivity;
import kzy.com.gyyengineer.leanchat.activity.MainActivity;
import kzy.com.gyyengineer.model.HistoryDataBean;

/**
 * 创建日期：2017/6/21 0021 on 15:37
 * 描述：
 * 作者：赵金祥  Administrator
 */
public class HomeHistoryAdapter extends BaseAdapter {
    private final MainActivity activity;
    private final HistoryDataBean dataBean;


    public HomeHistoryAdapter(FragmentActivity activity, HistoryDataBean dataBean) {
        this.activity = (MainActivity) activity;
        this.dataBean = dataBean;
    }

    @Override
    public int getCount() {
        return dataBean == null ? 0 : dataBean.getData().size();
    }

    @Override
    public HistoryDataBean.DataBean getItem(int position) {
        HistoryDataBean.DataBean dataBean = this.dataBean.getData().get(position);
        return dataBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder viewholder;
        if (convertView == null) {
            viewholder = new viewholder();
            convertView = View.inflate(activity, R.layout.history_lv_item, null);
            viewholder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewholder.tv_jqh = (TextView) convertView.findViewById(R.id.tv_jqh);
            viewholder.tv_gzms = (TextView) convertView.findViewById(R.id.tv_gzms);
            viewholder.tv_wxjl = (TextView) convertView.findViewById(R.id.tv_wxjl);
            viewholder.but_ckpj = (Button) convertView.findViewById(R.id.but_ckpj);
            viewholder.ll_ckpj = (LinearLayout) convertView.findViewById(R.id.ll_ckpj);
            convertView.setTag(viewholder);
        } else {
            viewholder = (HomeHistoryAdapter.viewholder) convertView.getTag();
        }
        final HistoryDataBean.DataBean dataBean = getItem(position);
        viewholder.tv_time.setText(dataBean.getRecordTime());
        viewholder.tv_jqh.setText(dataBean.getMarchineCode());
        viewholder.tv_gzms.setText(dataBean.getRepairDetails());
        viewholder.tv_wxjl.setText(dataBean.getRecordDetails());
        Log.e("HomeHistoryAdapter", "----维修记录----" + dataBean + "===========" + dataBean.getRepairDetails());
        String evaluateStatus = dataBean.getEvaluateStatus();
        if ("0".equals(evaluateStatus)) {
            viewholder.ll_ckpj.setVisibility(View.GONE);
        } else if ("1".equals(evaluateStatus)) {
            viewholder.ll_ckpj.setVisibility(View.VISIBLE);
            viewholder.but_ckpj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, SeeEvaluateActivity.class);
                    String cloudOrder = dataBean.getCloudOrder();
                    intent.putExtra("cloudOrder", cloudOrder);
                    activity.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    public class viewholder {
        public TextView tv_time;
        public TextView tv_jqh;
        public TextView tv_gzms;
        public TextView tv_wxjl;
        public Button but_ckpj;
        public LinearLayout ll_ckpj;
    }
}
