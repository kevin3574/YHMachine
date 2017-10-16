package kzy.com.gyyengineer.engineer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.adapter.HomeHistoryAdapter;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.HistoryDataBean;
import kzy.com.gyyengineer.utils.MyHttpUtils;

import static kzy.com.gyyengineer.leanchat.model.LeanchatUser.getCurrentUser;

/**
 * 创建日期：2017/6/20 0020 on 13:07
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class HistoryFragment extends Fragment {

    private View view;
    private ListView lv_history;
    private String name;
    HistoryDataBean historyDataBean;  // 网络获取 historyDataBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment_layout, null);
        initviews();
        initListener();
        initdatas();
        return view;
    }


    private void initviews() {
        lv_history = (ListView) view.findViewById(R.id.lv_history);
    }

    private void initListener() {

    }

    private void initdatas() {
        getDataInNet();
    }


    private void getDataInNet() {

        String url = Constants.URL + "cloundEngineer/historyRecord";
        if (getCurrentUser() != null) {
            name = getCurrentUser().getUsername();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("telephone", name.substring(1, name.length()));
        Log.e("HistoryFragment", "--name--" + name.substring(1, name.length()));
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Gson gson = new Gson();
                historyDataBean = gson.fromJson(result, HistoryDataBean.class);
                lv_history.setAdapter(new HomeHistoryAdapter(getActivity(), historyDataBean));

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.toast("网络异常，请稍后重试");
            }
        });
    }
}
