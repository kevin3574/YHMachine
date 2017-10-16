package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MsgInputActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.ll_top)
    RelativeLayout llTop;
    @Bind(R.id.tv_bar_code)
    TextView tvBarCode;
    @Bind(R.id.et_qymc)
    EditText etQymc;
    @Bind(R.id.et_qydz)
    EditText etQydz;
    @Bind(R.id.et_jqlx)
    EditText etJqlx;
    @Bind(R.id.et_jqxh)
    EditText etJqxh;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.activity_msg_input)
    LinearLayout activityMsgInput;
    @Bind(R.id.but_ok)
    Button butOk;
    @Bind(R.id.tv_jqxlh)
    TextView tvJqxlh;
    private String qymc;
    private String qydz;
    private String jqlx;
    private String jqxh;
    private String name;
    private String phone;
    private String marchineCode;
    private String result;
    private int MSGINPUT_OK = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题拦
        setContentView(R.layout.activity_msg_input);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        marchineCode = intent.getStringExtra("marchineCode");
        result = intent.getStringExtra("result");
        initlisteners();
        inittitles();
        initdtas();
        if (!TextUtils.isEmpty(result)) {
            resolveResult(result);
        }
    }

    private void resolveResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("status");
            if ("2".equals(status)) {
                tvBarCode.setText(jsonObject.getString("marchineCode"));
                tvJqxlh.setText(jsonObject.getString("marchineSerial"));
            } else if ("1".equals(status)) {
                tvBarCode.setText(jsonObject.getString("marchineCode"));
                tvJqxlh.setText(jsonObject.getString("marchineSerial"));
                etQymc.setText(jsonObject.getString("compamyName"));
                etQydz.setText(jsonObject.getString("compamyAddress"));
                etJqlx.setText(jsonObject.getString("marchineType"));
                etJqxh.setText(jsonObject.getString("marchineSerialCode"));
                etName.setText(jsonObject.getString("connectName"));
                etPhone.setText(jsonObject.getString("connectTelephone"));
            }
        } catch (JSONException e) {
        }
    }

    private void initlisteners() {
        back.setOnClickListener(this);
        btnTijao.setOnClickListener(this);
    }

    private void inittitles() {
        tvTop.setText("信息录入");
        butOk.setVisibility(View.GONE);
    }

    private void initdtas() {
        tvBarCode.setText(marchineCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_tijao:
                getEtText();
                if (TextUtils.isEmpty(qymc)) {
                    Util.showToast(MsgInputActivity.this, "请输入企业名称");
                    return;
                } else if (TextUtils.isEmpty(qydz)) {
                    Util.showToast(MsgInputActivity.this, "请输入企业地址");
                    return;
                } else if (TextUtils.isEmpty(jqlx)) {
                    Util.showToast(MsgInputActivity.this, "请输入机器类型");
                    return;
                } else if (TextUtils.isEmpty(jqxh)) {
                    Util.showToast(MsgInputActivity.this, "请输入机器型号");
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    Util.showToast(MsgInputActivity.this, "请输入联系人姓名");
                    return;
                } else if (TextUtils.isEmpty(phone)) {
                    Util.showToast(MsgInputActivity.this, "请输入联系人电话");
                    return;
                }
                String url = Constants.URL + "cloundEngineer/informationEntry";
                RequestParams params = new RequestParams();
                params.addBodyParameter("marchineCode", marchineCode);
                params.addBodyParameter("compamyName", qymc);
                params.addBodyParameter("compamyAddress", qydz);
                params.addBodyParameter("marchineSerialCode", jqxh);
                params.addBodyParameter("marchineType", jqlx);
                params.addBodyParameter("marchineSerial", tvJqxlh.getText().toString());
                params.addBodyParameter("connectName", name);
                params.addBodyParameter("connectTelephone", phone);
                Log.e("MsgInputActivity", marchineCode+"--"+qymc+"--"+qydz+"--"+jqxh+"--"+jqlx+"--"+tvJqxlh.getText().toString()+"--"+name+"--"+phone);
                MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String result = (String) responseInfo.result;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = (String) jsonObject.get("status");
                            String data = (String) jsonObject.get("data");
                            if ("1".equals(status)) {
                                Util.showToast(MsgInputActivity.this, data);
                            } else {
                                Util.showToast(MsgInputActivity.this, data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MsgInputActivity.this.setResult(MSGINPUT_OK);
                        MsgInputActivity.this.finish();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("MsgInputActivity", "--error--" + error + "--msg--" + msg);
                        Util.showToast(MsgInputActivity.this, "出现异常");
                    }
                });
                break;
        }
    }

    private void getEtText() {
        qymc = etQymc.getText().toString();
        qydz = etQydz.getText().toString();
        jqlx = etJqlx.getText().toString();
        jqxh = etJqxh.getText().toString();
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
    }
}
