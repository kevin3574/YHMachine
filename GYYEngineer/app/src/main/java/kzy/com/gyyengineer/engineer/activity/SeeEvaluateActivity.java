package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.Util;

public class SeeEvaluateActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_evaluate)
    TextView tvEvaluate;
    @Bind(R.id.rt_zhiliang)
    AppCompatRatingBar rtZhiliang;
    @Bind(R.id.rt_taidu)
    AppCompatRatingBar rtTaidu;
    @Bind(R.id.rt_zhuanye)
    AppCompatRatingBar rtZhuanye;
    @Bind(R.id.rt_jishi)
    AppCompatRatingBar rtJishi;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    private String cloudOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_evaluate);
        ButterKnife.bind(this);
        cloudOrder = getIntent().getStringExtra("cloudOrder");
        initlisteners();
        initdatas();
    }

    private void initlisteners() {
        back.setOnClickListener(this);
        btnTijao.setVisibility(View.GONE);
        tvTop.setText("查看评价");
    }

    private void initdatas() {
        String url = Constants.URL + "cloundEngineer/evaluate";
        RequestParams params = new RequestParams();
        params.addBodyParameter("cloudOrder", cloudOrder);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Log.e("SeeEvaluateActivity", "--result--" + result);
                try {
                    JSONObject jo = new JSONObject(result);
                    String status = (String) jo.get("status");
                    if ("1".equals(status)) {
                        String evaluate = (String) jo.get("evaluate");
                        String serviceQuality = (String) jo.get("serviceQuality");  // 服务质量
                        String serviceAttitude = (String) jo.get("serviceAttitude");// 服务态度
                        String profeesional = (String) jo.get("profeesional");// 专业
                        String timeliness = (String) jo.get("timeliness");// 及时性
                        String evaluateTime = (String) jo.get("evaluateTime");
                        tvTime.setText(evaluateTime);
                        tvEvaluate.setText(evaluate);
                        rtZhiliang.setRating(Float.valueOf(serviceQuality));
                        rtTaidu.setRating(Float.valueOf(serviceAttitude));
                        rtZhuanye.setRating(Float.valueOf(profeesional));
                        rtJishi.setRating(Float.valueOf(timeliness));
                    } else {
                        Util.showToast(SeeEvaluateActivity.this, "用户还没有评价");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Util.showToast(SeeEvaluateActivity.this, "用户还没有评价");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
