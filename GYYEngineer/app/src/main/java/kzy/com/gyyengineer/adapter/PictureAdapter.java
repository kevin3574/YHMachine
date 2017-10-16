package kzy.com.gyyengineer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import kzy.com.gyyengineer.R;

import static kzy.com.gyyengineer.utils.PhotoUtils.avatarImageOption;

/**
 * 创建日期：2017/6/26 0026 on 9:29
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class PictureAdapter extends BaseAdapter {

    private final List<String> list;
    private Context context;
    private final BitmapUtils bitmapUtils;


    public PictureAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.iv_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.e("DetailOneActivity", "list.get(position)----" + list.get(position));
        //       bitmapUtils.display(viewHolder.iv, list.get(position));
        ImageLoader.getInstance().displayImage(list.get(position), viewHolder.iv, avatarImageOption);
        return convertView;
    }

    public class ViewHolder {
        private ImageView iv;
    }
}
