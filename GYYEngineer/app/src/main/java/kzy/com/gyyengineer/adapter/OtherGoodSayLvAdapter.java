package kzy.com.gyyengineer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.model.OtherGoodSayBean;

import static kzy.com.gyyengineer.utils.PhotoUtils.avatarImageOption;

/**
 * 创建日期：2017/7/12 0012 on 14:42
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class OtherGoodSayLvAdapter extends BaseAdapter {
    private final Context context;
    private final List<OtherGoodSayBean.DataBean> dataBeans;

    public OtherGoodSayLvAdapter(Context context, List<OtherGoodSayBean.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @Override
    public int getCount() {
        return dataBeans == null ? 0 : dataBeans.size();
    }

    @Override
    public OtherGoodSayBean.DataBean getItem(int position) {
        OtherGoodSayBean.DataBean dataBean = dataBeans.get(position);
        return dataBean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.goodsay_lv_item_layout, null);
            holder = new ViewHolder();
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.iv_handView = (ImageView) convertView.findViewById(R.id.iv_handView);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OtherGoodSayBean.DataBean dataBean = getItem(position);
        holder.tv_num.setText(dataBean.getRank());
        holder.tv_name.setText(dataBean.getEngineer());
        holder.tv_percent.setText(dataBean.getRate()+"%");
        ImageLoader.getInstance().displayImage(dataBean.getEngineerImage(), holder.iv_handView, avatarImageOption);

        return convertView;
    }

    public class ViewHolder {
        public TextView tv_num;
        public TextView tv_name;
        public TextView tv_percent;
        public ImageView iv_handView;
    }
}
