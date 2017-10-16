package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.utils.MyHttpUtils;

public class MsgInputAndNumberActivity extends BaseActivity {

    private static final int FINISH_MsgInputAndNumberActivity = 124;
    @Bind(R.id.ll_back)
    LinearLayout llBack;
    @Bind(R.id.et_jqxlh)
    EditText etJqxlh;
    @Bind(R.id.but_next)
    Button butNext;
    private String resultString;
    private int START_MsgInputActivity_REQUESTCODE = 122;
    private int MSGINPUT_OK = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_input_and_number);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        resultString = intent.getStringExtra("resultString");
        initListeners();
    }

    private void initListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        butNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subData2Net();
            }
        });
    }

    private void subData2Net() {
        String s = etJqxlh.getText().toString();
        if (TextUtils.isEmpty(s)) {
            Utils.toast("请输入机器序列号");
            return;
        }
        String url = Constants.URL + "cloundEngineer/informationScanning";
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("marchineSerial", s);
        requestParams.addBodyParameter("marchineCode", resultString);
        Log.e("MsgInputAndNumberActivity", "--marchineSerial--" + s + "--marchineCode--" + resultString);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Intent intent = new Intent(MsgInputAndNumberActivity.this, MsgInputActivity.class);
                Log.e("MsgInputAndNumberActivity", "--result--" + result );
                intent.putExtra("result", result);
                intent.putExtra("marchineCode", resultString);
                startActivityForResult(intent, START_MsgInputActivity_REQUESTCODE);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.toast("网络异常，请稍候");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_MsgInputActivity_REQUESTCODE && resultCode == MSGINPUT_OK) {
            MsgInputAndNumberActivity.this.setResult(FINISH_MsgInputAndNumberActivity);
            MsgInputAndNumberActivity.this.finish();
        }
    }
}
