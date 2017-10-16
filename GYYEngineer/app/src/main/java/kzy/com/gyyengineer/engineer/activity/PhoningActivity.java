package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PhoningActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.yjj)
    TextView yjj;
    @Bind(R.id.wjj)
    TextView wjj;
    @Bind(R.id.et_wxxq)
    EditText etWxxq;
    @Bind(R.id.but_ok)
    Button butOk;
    @Bind(R.id.activity_phoning)
    LinearLayout activityPhoning;
    @Bind(R.id.tv_pgdh)
    TextView tvPgdh;
    @Bind(R.id.tv_jqbh)
    TextView tvJqbh;
    @Bind(R.id.ll_pgdh)
    LinearLayout llPgdh;
    @Bind(R.id.ll_jqbh)
    LinearLayout llJqbh;
    private String jqbh;
    private boolean iswjj;
    private String cloudOrder;

    private int SpotActivity_FINISH = 010;  // 请求码  当未解决页面关闭时候执行
    private int SpotActivity_FINISH_RESULT = 011; // 结果码  当未解决页面关闭时候执行
    private int PhoningActivity_FINISH_RESULT = 201; //  结果码   运行状态界面提交之后返回

    private int PhoningActivity_FINISH = 020;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e("PhoningActivity", "msg.obj--" + msg.obj.toString());
                    RepairBean repairBean = (RepairBean) msg.obj;
                    String status = repairBean.getStatus();
                    int marchineExit = repairBean.getMarchineExit();
                    if ("1".equals(status)) {
                        if (!iswjj) {
// 选择了已解决
                            setResult(PhoningActivity_FINISH_RESULT);
                            PhoningActivity.this.finish();
                            Utils.toast("提交完成");
                        } else {
// 选择了未解决

                            Intent intent = new Intent(PhoningActivity.this, SpotActivity.class);
                            intent.putExtra("jqbh", jqbh);
                            intent.putExtra("cloudOrder", cloudOrder);
                            intent.putExtra("marchineExit", marchineExit);
                            intent.putExtra("repairOrder", repairBean.getRepairOrder());

                            startActivityForResult(intent, SpotActivity_FINISH);
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoning);
        ButterKnife.bind(this);
        jqbh = getIntent().getStringExtra("jqbh");
        cloudOrder = getIntent().getStringExtra("cloudOrder");
        switchisyjj(iswjj);
        initviews();
        initlisteners();
    }

    private void initviews() {
        tvTop.setText("电话沟通");
        btnTijao.setVisibility(View.GONE);
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
                    sbuData2Net(cloudOrder, "0", "ok", "", "");
                } else {
// 选择了未解决
                    sbuData2Net(cloudOrder, "1", "", "", "");
                    Log.e("PhoningActivity", "未解决iswjj--" + iswjj);
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

    // 访问网络     "ok"代表需要向服务器传递参数，否则不传递参数
    private void sbuData2Net(String cloudOrder, final String recordResult, String iswxxq, String repairOrder, String marchineCode) {
        String wxxq = etWxxq.getText().toString();
        if (TextUtils.isEmpty(wxxq)) {
            Utils.toast("请输入维修详情");
            return;
        }
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
            requestParams.addBodyParameter("marchineCode", marchineCode);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SpotActivity_FINISH) {
            setResult(SpotActivity_FINISH_RESULT);
            finish();
        }
        if (requestCode == PhoningActivity_FINISH && resultCode == PhoningActivity_FINISH_RESULT) {
            // 电话解决界面点击提交按钮完成
            // 设置结果码  关闭详情页

            finish();
        }
    }
}
