package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.jpush.ExampleUtil;
import kzy.com.gyyengineer.jpush.TagAliasOperatorHelper;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.PreferenceUtils;

import static com.avos.avoscloud.AVUser.getCurrentUser;
import static kzy.com.gyyengineer.jpush.TagAliasOperatorHelper.ACTION_SET;
import static kzy.com.gyyengineer.jpush.TagAliasOperatorHelper.sequence;

public class UserMachineActivity extends AppCompatActivity {
    @Bind(R.id.btn_tijao)
    Button btntijao;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.ll_top)
    RelativeLayout llTop;
    @Bind(R.id.et_machineNum)
    EditText etMachineNum;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_twoPssword)
    EditText etTwoPassword;
    @Bind(R.id.et_zhuce)
    Button etZhuce;
    @Bind(R.id.activity_user_machine)
    LinearLayout activityUserMachine;
    private String password;
    private String name;
    private String machineNUm;
    private String phone;
    private String twoPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_machine);
        ButterKnife.bind(this);
        tvTop.setText("用户注册");
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        //  phone = "15719212113";
        PreferenceUtils.setPrefString(this, Constants.MYPHONE, phone);
        btntijao.setVisibility(View.GONE);
    }


    @OnClick({R.id.back, R.id.et_zhuce})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.et_zhuce:
                //createAccount(phone, password);
                machineNUm = etMachineNum.getText().toString().trim();
                name = etName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                twoPassword = etTwoPassword.getText().toString().trim();

                if (password.length() < 6 || twoPassword.length() < 6) {
                    Toast.makeText(this, "请输入6位或者6位以上密码", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(twoPassword)) {
                    Toast.makeText(this, "请确认密码是否输入一致", Toast.LENGTH_SHORT).show();
                } else {
                    creaSerAccount(phone, password, name, machineNUm);
                }
                break;
        }
    }

    public static String getCurrentUserId() {
        LeanchatUser currentUser = getCurrentUser(LeanchatUser.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }

    private void creaSerAccount(final String phone, final String password, final String name, final String machineNUm) {


        String url = Constants.URL + "cloundEngineer/userRegister";
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("telephone", phone);
        requestParams.addBodyParameter("password", password);
        requestParams.addBodyParameter("userName", name);
        requestParams.addBodyParameter("engineerCode", machineNUm);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = (String) jsonObject.get("status");
                    if (status.equals("2") || status.equals("1") || status.equals("0")) {

                        LeanchatUser.signUpByNameAndPwdAndProperty("D" + phone, password, "engineer", new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e != null) {
                                    Utils.toast(App.getInstance().getString(
                                            R.string.registerFailed)
                                    );
                                    Log.e("LoginActivity", "----注册失败00---" + e.getMessage());
                                } else {
// 内部工程师注册成功   jpush设置tag
                                    int action = -1;
                                    String tags = phone;
                                    if (tags == null) {
                                        return;
                                    }
                                    action = ACTION_SET;
                                    setjPushAction(phone, action);
                                    Utils.toast("注册成功");
                                    startActivity(new Intent(UserMachineActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else if (status.equals("3")) {
                        Utils.toast("请输入正确的用户名和员工代码");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.toast("网络异常，请稍候再试");
                Log.e("EntrySplashActivity", "------内部注册------" + error + "        " + msg);
            }
        });


    }

    private void setjPushAction(String tags, int action) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        tagAliasBean.tags = getInPutTags(tags);
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
    }

    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private Set<String> getInPutTags(String tags) {
        String tag = tags;
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                return null;
            }
            tagSet.add(sTagItme);
        }
        if (tagSet.isEmpty()) {
            return null;
        }
        return tagSet;
    }
}
