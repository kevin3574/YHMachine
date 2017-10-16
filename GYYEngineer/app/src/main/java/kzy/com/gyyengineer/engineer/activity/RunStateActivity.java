package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

public class RunStateActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.ll_top)
    RelativeLayout llTop;
    @Bind(R.id.et_zxyl)
    EditText etZxyl;
    @Bind(R.id.et_jzyl)
    EditText etJzyl;
    @Bind(R.id.et_zxgy)
    EditText etZxgy;
    @Bind(R.id.et_yxsj)
    EditText etYxsj;
    @Bind(R.id.et_jzsj)
    EditText etJzsj;
    @Bind(R.id.rb_11)
    RadioButton rb11;
    @Bind(R.id.rb_12)
    RadioButton rb12;
    @Bind(R.id.rg_1)
    RadioGroup rg1;
    @Bind(R.id.rb_21)
    RadioButton rb21;
    @Bind(R.id.rb_22)
    RadioButton rb22;
    @Bind(R.id.rg_2)
    RadioGroup rg2;
    @Bind(R.id.rb_31)
    RadioButton rb31;
    @Bind(R.id.rb_32)
    RadioButton rb32;
    @Bind(R.id.rg_3)
    RadioGroup rg3;
    @Bind(R.id.et_hjwd)
    EditText etHjwd;
    @Bind(R.id.et_pqwd)
    EditText etPqwd;
    @Bind(R.id.et_dl1)
    EditText etDl1;
    @Bind(R.id.et_dl2)
    EditText etDl2;
    @Bind(R.id.et_dl3)
    EditText etDl3;
    @Bind(R.id.et_dl4)
    EditText etDl4;
    @Bind(R.id.but_ok)
    Button butOk;
    @Bind(R.id.activity_run_state)
    LinearLayout activityRunState;
    @Bind(R.id.et_dy1)
    EditText etDy1;
    @Bind(R.id.et_dy2)
    EditText etDy2;
    @Bind(R.id.et_dy3)
    EditText etDy3;
    private String state_klyc;    // 空滤压差
    private String state_ylyc;    // 油滤压差
    private String state_yfyc;    // 油分压差


    private int PhoningActivity_FINISH_RESULT = 201; //  结果码   运行状态界面提交之后返回
    private Object sbuData;
    private String cloudOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_state);
        ButterKnife.bind(this);
        cloudOrder = getIntent().getStringExtra("cloudOrder");
        initviews();
        initlisteners();
    }

    private void initlisteners() {
        back.setOnClickListener(this);
        butOk.setOnClickListener(this);
        // 空滤压差
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_11:
                        state_klyc = "0";
                        rb11.setChecked(true);
                        rb12.setChecked(false);
                        break;
                    case R.id.rb_12:
                        state_klyc = "1";
                        rb11.setChecked(false);
                        rb12.setChecked(true);
                        break;
                }
            }
        });
        // 油滤压差
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_21:
                        state_ylyc = "0";
                        rb21.setChecked(true);
                        rb22.setChecked(false);
                        break;
                    case R.id.rb_22:
                        state_ylyc = "1";
                        rb21.setChecked(false);
                        rb22.setChecked(true);
                        break;
                }
            }
        });
        // 油分压差
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_31:
                        state_yfyc = "0";
                        rb31.setChecked(true);
                        rb32.setChecked(false);
                        break;
                    case R.id.rb_32:
                        state_yfyc = "1";
                        rb31.setChecked(false);
                        rb32.setChecked(true);
                        break;
                }
            }
        });

    }

    private void initviews() {
        rb11.setChecked(true);
        rb21.setChecked(true);
        rb31.setChecked(true);
        tvTop.setText("设备运行状况");
        btnTijao.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.but_ok:
                getSbuData();
                break;
        }
    }

    public void getSbuData() {
        String Xzyl = etZxyl.getText().toString();
        String Hjwd = etHjwd.getText().toString();
        String Zxgy = etZxgy.getText().toString();
        String Yxsj = etYxsj.getText().toString();
        String Dl1 = etDl1.getText().toString();
        String Dl2 = etDl2.getText().toString();
        String Dl3 = etDl3.getText().toString();
        String Dl4 = etDl4.getText().toString();
        String Pqwd = etPqwd.getText().toString();
        String Jzyl = etJzyl.getText().toString();
        String Jzsj = etJzsj.getText().toString();
        String Dy1 = etDy1.getText().toString();
        String Dy2 = etDy2.getText().toString();
        String Dy3 = etDy3.getText().toString();


        if (TextUtils.isEmpty(Xzyl)) {
            Util.showToast(RunStateActivity.this, "请填写卸载压力");
            return;
        } else if (TextUtils.isEmpty(Hjwd)) {
            Util.showToast(RunStateActivity.this, "请填写环境温度");
            return;
        } else if (TextUtils.isEmpty(Dy1) || TextUtils.isEmpty(Dy2) || TextUtils.isEmpty(Dy3)) {
            Util.showToast(RunStateActivity.this, "请填写电压");
            return;
        } else if (TextUtils.isEmpty(Zxgy)) {
            Util.showToast(RunStateActivity.this, "请填写最大罐压");
            return;
        } else if (TextUtils.isEmpty(Yxsj)) {
            Util.showToast(RunStateActivity.this, "请填写运行时间");
            return;
        } else if (TextUtils.isEmpty(Pqwd)) {
            Util.showToast(RunStateActivity.this, "请填写排气温度");
            return;
        } else if (TextUtils.isEmpty(Jzyl)) {
            Util.showToast(RunStateActivity.this, "请填写加载压力");
            return;
        } else if (TextUtils.isEmpty(Jzsj)) {
            Util.showToast(RunStateActivity.this, "请填写加载时间");
            return;
        } else if (TextUtils.isEmpty(Dl1) || TextUtils.isEmpty(Dl2) || TextUtils.isEmpty(Dl3) || TextUtils.isEmpty(Dl4)) {
            Util.showToast(RunStateActivity.this, "请填写电流");
            return;
        }
        String DL = Dl1 + "bar" + Dl2 + "A" + Dl3 + "A" + Dl4 + "A";   // 电流
        String DY = Dy1 + "V" + Dy2 + "V" + Dy3 + "V";                 // 电压

        String url = Constants.URL + "cloundEngineer/repairMarchine";
        RequestParams params = new RequestParams();
        params.addBodyParameter("cloudOrder", cloudOrder);
        params.addBodyParameter("dischargePressure", Xzyl);
        params.addBodyParameter("environment", Hjwd);
        params.addBodyParameter("voltage", DY);
        params.addBodyParameter("emptyPressure", state_klyc);
        params.addBodyParameter("oilDivPressure", state_yfyc);
        params.addBodyParameter("oilPressure", state_ylyc);
        params.addBodyParameter("miniPressure", Zxgy);
        params.addBodyParameter("runTime", Yxsj);
        params.addBodyParameter("electricity", DL);
        params.addBodyParameter("dischargeTemper", Pqwd);
        params.addBodyParameter("loadPressure", Jzyl);
        params.addBodyParameter("loadTime", Yxsj);
        sbuData2Net(url, params);
    }

    private void sbuData2Net(String url, RequestParams params) {
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                try {
                    JSONObject jo = new JSONObject(result);
                    String status = (String) jo.get("status");
                    String data = (String) jo.get("data");
                    if ("1".equals(status)) {
                        Util.showToast(RunStateActivity.this, data);
                        setResult(PhoningActivity_FINISH_RESULT);
                        finish();
                    } else {
                        Util.showToast(RunStateActivity.this, data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Util.showToast(RunStateActivity.this, "提交失败，请检查您的网络");
            }
        });
    }

}
