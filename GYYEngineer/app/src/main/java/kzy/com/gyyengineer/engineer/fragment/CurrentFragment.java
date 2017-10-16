package kzy.com.gyyengineer.engineer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.adapter.HomeCurrentLvAdapter;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.engineer.activity.DetailOneActivity;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.CurrentDataBean;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.view.CirclePercentView;

import static kzy.com.gyyengineer.leanchat.model.LeanchatUser.getCurrentUser;

/**
 * 创建日期：2017/6/20 0020 on 13:07
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class CurrentFragment extends Fragment implements View.OnClickListener {
    private View view;
    private View top_view;
    private ListView lv;
    private CirclePercentView circleView;
    private Button but_ouder;
    private boolean ismonth;
    private TextView ouder_tv;
    private String name;
    int sumCount;
    int monthCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.current_fragment_layout, null);
        top_view = LayoutInflater.from(getActivity()).inflate(R.layout.fargment_home_top_layout, null);

        initviews();
        inirListeners();
        initdatas();
        return view;
    }

    private void initviews() {
        lv = (ListView) view.findViewById(R.id.current_lv);
        lv.addHeaderView(top_view);
        circleView = (CirclePercentView) top_view.findViewById(R.id.circleView);
        but_ouder = (Button) top_view.findViewById(R.id.but_ouder);
        ouder_tv = (TextView) top_view.findViewById(R.id.order_num);
    }

    private void inirListeners() {
        but_ouder.setOnClickListener(this);
    }

    private void initdatas() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getDataInNet(!ismonth);
        getCurrentData();
    }

    private CurrentDataBean resolveCurrentData(String currentData) {
        if (currentData == null) {
            return null;
        }
        Gson gson = new Gson();
        CurrentDataBean currentDataBean = gson.fromJson(currentData, CurrentDataBean.class);
        return currentDataBean;
    }

    // 获取首页当前报修记录数据
    private void getCurrentData() {
        String url = Constants.URL + "cloundEngineer/repairCurrentRecord";
        RequestParams requestParams = new RequestParams();
        if (getCurrentUser() != null) {
            name = getCurrentUser().getUsername();
            requestParams.addBodyParameter("telephone", name.substring(1, name.length()));
        }else {
            return;
        }
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {

            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                final String CurrentData = (String) responseInfo.result;
                CurrentDataBean currentDataBean = resolveCurrentData(CurrentData);
                Log.e("HomeCurrentLvAdapter", "--6.1--" + CurrentData);
                lv.setAdapter(new HomeCurrentLvAdapter(getActivity(), currentDataBean));
                // 控制首页订单数

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            return;
                        }
                        String cloudOrder = resolveCurrentData(CurrentData).getData().get(position - 1).getCloudOrder();
                        String status = resolveCurrentData(CurrentData).getStatus();
                        Intent intent = new Intent(getActivity(), DetailOneActivity.class);
                        String orderStatus = resolveCurrentData(CurrentData).getData().get(position - 1).getOrderStatus();
                        intent.putExtra("statu", status);
                        intent.putExtra("cloudOrder", cloudOrder);
                        if ("0".equals(status)) {
                            intent.putExtra("status", "1");
                        } else if ("1".equals(status)) {
                            intent.putExtra("status", orderStatus);
                        }
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {Utils.toast("网络异常");

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_ouder:
                ismonth = !ismonth;
                getDataInNet(ismonth);
                break;
        }
    }

    private void switchOuderNum(boolean ismonth, int oudernum) {
        if (!ismonth) {
            but_ouder.setBackgroundResource(R.drawable.month_but);
            ouder_tv.setText("本月服务订单数");
            circleView.invalidate();
        } else {
            but_ouder.setBackgroundResource(R.drawable.arr_but);
            ouder_tv.setText("总服务订单数");
            circleView.setPercent(oudernum + 1);
            circleView.invalidate();
        }


    }

    //获取网络数据
    public void getDataInNet(final boolean ismonth) {
        String url = Constants.URL + "cloundEngineer/repairCount";
        if (getCurrentUser() != null) {
            name = getCurrentUser().getUsername();
        }else {
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("telephone", name.substring(1, name.length()));
        if (!ismonth) {
            params.addBodyParameter("status", "1");
            MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {

                @Override
                public void onSuccess(ResponseInfo responseInfo) {
                    String result = (String) responseInfo.result;
                    Log.e("CurrentFragment","--总订单数01--"+result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.get("status") == "1") {
                            monthCount = jsonObject.getInt("count");
                        } else {
                            monthCount = jsonObject.getInt("count");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    switchOuderNum(ismonth, monthCount);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    monthCount = 0;
                }
            });
        } else {
            params.addBodyParameter("status", "0");
            MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {

                @Override
                public void onSuccess(ResponseInfo responseInfo) {
                    String result = (String) responseInfo.result;
                    Log.e("CurrentFragment","--总订单数00--"+result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if ("1".equals(jsonObject.get("status"))) {
                            sumCount = jsonObject.getInt("count");
                        } else {
                            sumCount = jsonObject.getInt("count");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    switchOuderNum(ismonth, sumCount);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Utils.toast("网络异常");
                    sumCount = 0;
                }
            });
        }
    }
}
