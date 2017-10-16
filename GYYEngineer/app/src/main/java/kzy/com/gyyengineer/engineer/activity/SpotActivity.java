package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.model.RepairBean;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.utils.MyHttpUtils;

public class SpotActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.ll_top)
    RelativeLayout llTop;
    @Bind(R.id.yjj)
    TextView yjj;
    @Bind(R.id.wjj)
    TextView wjj;
    @Bind(R.id.et_wxxq)
    EditText etWxxq;
    @Bind(R.id.tv_pgdh)
    TextView tvPgdh;
    @Bind(R.id.ll_pgdh)
    LinearLayout llPgdh;
    @Bind(R.id.tv_jqbh)
    TextView tvJqbh;
    @Bind(R.id.ll_jqbh)
    LinearLayout llJqbh;
    @Bind(R.id.but_ok)
    Button butOk;
    @Bind(R.id.activity_phoning)
    LinearLayout activityPhoning;
    private String jqbh;
    private String cloudOrder;
    private boolean iswjj;
    private int marchineExit;
    private String repairOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        ButterKnife.bind(this);
        jqbh = getIntent().getStringExtra("jqbh");
        cloudOrder = getIntent().getStringExtra("cloudOrder");
        marchineExit = getIntent().getIntExtra("marchineExit", 0);
        repairOrder = getIntent().getStringExtra("repairOrder");

        switchisyjj(iswjj);
        initviews();
        initlisteners();
    }

    private void initlisteners() {


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!iswjj) {
// 选择了已解决
                    Log.e("PhoningActivity", "解决iswjj--" + iswjj);
                    if (marchineExit == 0) {
// 显示机器编码
                        sbuData2Net(cloudOrder, "2", "ok", "ok", "ok");
                    } else if (marchineExit == 1) {
                        sbuData2Net(cloudOrder, "2", "ok", "ok", "");
                    }
                } else {
// 选择了未解决

                    if (marchineExit == 0) {
// 显示机器编码
                        sbuData2Net(cloudOrder, "3", "", "ok", "ok");
                    } else if (marchineExit == 1) {
                        sbuData2Net(cloudOrder, "3", "", "ok", "");
                    }
                }
            }
        });
        yjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iswjj = false;
                switchisyjj(iswjj);
            }
        });
        wjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iswjj = true;
                switchisyjj(iswjj);
            }
        });
    }

    private void initviews() {
        tvPgdh.setText(repairOrder);
        tvTop.setText("现场解决");
        btnTijao.setVisibility(View.GONE);
        if (marchineExit == 0) {
            llJqbh.setVisibility(View.VISIBLE);
        } else if (marchineExit == 1) {
            llJqbh.setVisibility(View.GONE);
        }
    }

    private void switchisyjj(boolean iswjj) {
        if (!iswjj) {
            yjj.setSelected(false);
            wjj.setSelected(true);
            butOk.setText("提交");
        } else {
            yjj.setSelected(true);
            butOk.setText("下一步");
            wjj.setSelected(false);
        }
    }

    // 访问网络     "ok"代表需要向服务器传递参数，否则不传递参数
    private void sbuData2Net(String cloudOrder, final String recordResult, String iswxxq, String repairOrder1, String marchineCode) {
        String wxxq = etWxxq.getText().toString();
        String etjqbh = tvJqbh.getText().toString();
        String url = Constants.URL + "cloundEngineer/marchineSolve";
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("cloudOrder", cloudOrder);
        if ("0".equals(recordResult)) {
            requestParams.addBodyParameter("recordResult", "0");
        } else if ("1".equals(recordResult)) {
            requestParams.addBodyParameter("recordResult", "1");
        } else if ("2".equals(recordResult)) {
            requestParams.addBodyParameter("recordResult", "2");
        } else if ("3".equals(recordResult)) {
            requestParams.addBodyParameter("recordResult", "3");
        }
        if ("ok".equals(iswxxq)) {
            requestParams.addBodyParameter("faultDescription", wxxq);
        }
        if ("ok".equals(repairOrder)) {
            requestParams.addBodyParameter("repairOrder", repairOrder);
        }
        if ("ok".equals(marchineCode)) {

            requestParams.addBodyParameter("marchineCode", etjqbh);
        }
        Log.e("PhoningActivity", "casnhu--" + cloudOrder);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Log.e("PhoningActivity", "result--" + result);
                Gson gson = new Gson();
                RepairBean repairBean = gson.fromJson(result, RepairBean.class);
                Message message = new Message();
                message.what = 1;
                message.obj = repairBean;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("PhoningActivity", "resultshibai--" + msg);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e("PhoningActivity", "msg.obj--" + msg.obj.toString());
                    RepairBean repairBean = (RepairBean) msg.obj;
                    String status = repairBean.getStatus();
                    if ("1".equals(status)) {
                        if (!iswjj) {
// 选择了已解决
                            Intent intent = new Intent(SpotActivity.this, RunStateActivity.class);
                            intent.putExtra("cloudOrder", cloudOrder);
                            startActivity(intent);
                            finish();
                            Utils.toast("提交完成");
                        } else {
// 选择了未解决
                            finish();
                        }
                    }
                    break;
            }
        }
    };
}
