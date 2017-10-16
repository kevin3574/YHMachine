package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.activity.MainActivity;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.ForGetpass;
import kzy.com.gyyengineer.model.UserInfo;
import kzy.com.gyyengineer.model.UserReg;
import kzy.com.gyyengineer.utils.MyAndroidUtil;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.PreferenceUtils;
import kzy.com.gyyengineer.utils.Tool;
import kzy.com.gyyengineer.utils.Util;

import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST;
import static kzy.com.gyyengineer.leanchat.model.LeanchatUser.getCurrentUserId;


/**
 * 创建人：赵金祥
 * 用户注册手机号验证页面
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private static final int SHORT_RESULT_CODE = 100;
    private EditText et_phone, et_code;
    private Button Message_btn, register_btn;
    private ImageView btn_back;
    private String flag;
    private String userPhone;
    private String phonecode;
    private String forPassOld;
    private Object forgetpwdinNet;
    private String name;
    private String pwd;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        initSms();
        initView();
        initEvent();
    }

    private void initEvent() {
        register_btn.setOnClickListener(this);
        Message_btn.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        Message_btn = (Button) findViewById(R.id.Message_btn);
        register_btn = (Button) findViewById(R.id.register_btn);
        btn_back = (ImageView) findViewById(R.id.back);
    }

    private void initSms() {
        MobSDK.init(this, "1f7a35b551d40", "6da2cdddefaefb2a0f25b85b4e6c79c2");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 16://忘记密码

                    final ForGetpass forGetpass = (ForGetpass) msg.obj;
                    Log.e("LoginActivity", "--00--" + forGetpass.getStatus() + "--01--" + msg.obj);
                    if (forGetpass.getStatus().equals("1")) {
                        forPassOld = forGetpass.getPassword();
                        if ("reg".equals(flag)) {
                            Tool.initToast(RegActivity.this, "该账号已注册过");
                        } else {
                            sendMess();
                        }

                    } else {
                        if ("reg".equals(flag)) {
                            sendMess();
                        } else {
                            Tool.initToast(RegActivity.this, "该账号未注册过");
                        }
                    }
                    break;
                case 29://手机界面
                    final UserReg userReg = (UserReg) msg.obj;
                    if (userReg.getStatus().equals("1")) {
                        Log.d("", "handleMessage: ");
                    } else if (userReg.getStatus().equals("0")) {
                        Utils.toast("企业代码不匹配");
                    } else if (userReg.getStatus().equals("2")) {
                        Utils.toast("手机号码已注册！");
                    } else if (userReg.getStatus().equals("3")) {
                        Utils.toast("手机号码可以注册");
                    }
                    break;
                case 01:
                    String result = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = (String) jsonObject.get("status");
                        if (status.equals("1") || status.equals("2")) {
                            if (status.equals("2")) {
                                PreferenceUtils.setPrefString(RegActivity.this, "one_log", "one_log");
                            } else {
                                PreferenceUtils.setPrefString(RegActivity.this, "one_log", "");
                            }
                            name = (String) jsonObject.get("telephone");
                            pwd = (String) jsonObject.get("password");
                            ShortLog(name, pwd);
                            Log.e("LoginActivity", "-----name----" + name + "---pwd---" + pwd);
                        } else {
                            Toast.makeText(RegActivity.this, "用户不存在！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 12:
                    UserReg userReg1 = (UserReg) msg.obj;
                    Log.e("LoginActivity", "-----02----" + userReg1.getStatus() + "-----03----" + userReg1.getData());

                    if (userReg1.getStatus().equals("1")) {
                        Toast.makeText(RegActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        if (Integer.valueOf(userReg1.getStatus()) == 2 || Integer.valueOf(userReg1.getStatus()) == 1) {
                            MyAndroidUtil.editXmlByString(Constants.LOGIN_CHECK, Integer.valueOf(userReg1.getStatus()) + "");
                            PreferenceUtils.setPrefString(getApplicationContext(), "phone", name);
                            finishLogin(name);
                        } else if (userReg1.getStatus().equals("0")) {
                            Toast.makeText(RegActivity.this, "用户不存在返回值是0", Toast.LENGTH_LONG).show();
                        } else {
                            if (Integer.valueOf(userReg1.getStatus()) == 3) {
                                Toast.makeText(RegActivity.this, "请输入密码！", Toast.LENGTH_LONG).show();
                            }
                            MyAndroidUtil.editXmlByString(Constants.LOGIN_CHECK, Integer.valueOf(userReg1.getStatus()) + "");
                            Intent intent = new Intent(RegActivity.this, TextActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(RegActivity.this, userReg1.getData() + "登录失败！", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };


    private void loginSerAccount(final String name, final String pwd) {
        final String url = Constants.SERVER_URL + "userLogin";
        LeanchatUser.logInInBackground(name, pwd,
                new LogInCallback<LeanchatUser>() {
                    @Override
                    public void done(LeanchatUser avUser, AVException e) {
                        if (e == null) {
                            Log.e("LoginActivity", "-----快捷登录成功----");
                            String currentUserId = getCurrentUserId();
                            UserInfo userInfo = new UserInfo();
                            userInfo.setPhone(name);
                            userInfo.setPass(pwd);
                            userInfo.setObjectId(currentUserId);
                            LeanchatUser.getCurrentUser().setMobilePhoneNumber(name);
                            MyHttpUtils.handData(handler, 12, url, userInfo);
                        } else {
                            Utils.toast(App.getInstance().getString(
                                    R.string.registerFailed)
                                    + e.getMessage());
                            Log.e("LoginActivity", "-----登录失败----");
                        }
                    }
                }, LeanchatUser.class);

    }

    @Override
    public void onClick(View v) {
        userPhone = et_phone.getText().toString();
        phonecode = et_code.getText().toString();
        switch (v.getId()) {
            case R.id.Message_btn:
                if (Util.getInstance().isMobileNumber(userPhone)) {
                   /* String uri = Constants.SERVER_URL + "userOldPassword";
                    UserInfo userInfo = new UserInfo();
                    userInfo.setPhone(userPhone + "");
                    MyHttpUtils.handData(handler, 16, uri, userInfo);*/
                    sendMess();
                } else {
                    Toast.makeText(this, "请输入11位有效手机号码", Toast.LENGTH_SHORT).show();
                    //sendMess();
                }
                break;
            case R.id.register_btn:
                if (Util.getInstance().isMobileNumber(userPhone) && phonecode.length() == 4) {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("appkey", "1f7a35b551d40");
                    params.addBodyParameter("phone", userPhone);
                    params.addBodyParameter("zone", "86");
                    params.addBodyParameter("code", phonecode);
                    new HttpUtils().send(POST, "https://webapi.sms.mob.com/sms/verify",
                            params, new RequestCallBack<Object>() {
                                @Override
                                public void onSuccess(ResponseInfo<Object> responseInfo) {
                                    try {
                                        JSONObject object = new JSONObject(responseInfo.result.toString());
                                        String s = object.getString("status");
                                        if ("200".equals(s)) {
                                            if ("for".equals(flag)) {
                                                Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                                                intent.putExtra("phone", userPhone);
                                                intent.putExtra("flag", flag);
                                                startActivity(intent);

                                            } else if ("reg".equals(flag)) {
                                                // 验证手机号码成功，查询是不是公司工程师
                                                String url = Constants.URL + "cloundEngineer/userRegister";
                                                RequestParams requestParams = new RequestParams();
                                                requestParams.addBodyParameter("telephone", userPhone);
                                                MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
                                                    @Override
                                                    public void onSuccess(ResponseInfo responseInfo) {
                                                        String result = (String) responseInfo.result;
                                                        Log.e("LoginActivity", "--验证手机号码成功，查询是不是公司工程师--" + result);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(result);
                                                            String status = (String) jsonObject.get("status");
                                                            if (status.equals("2")) {
                                                                Intent intent = new Intent(getApplicationContext(), UserMachineActivity.class);
                                                                intent.putExtra("phone", userPhone);
                                                                startActivity(intent);
                                                                finish();
                                                            } else if (status.equals("1") || status.equals("0")) {
                                                                Toast.makeText(RegActivity.this, (String) jsonObject.get("data"), Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                intent.putExtra("phone", userPhone);
                                                                startActivity(intent);
                                                                finish();
                                                            } else if (status.equals("4")) {
                                                                Toast.makeText(RegActivity.this, (String) jsonObject.get("data"), Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), AuditActivity.class);
                                                                intent.putExtra("phone", userPhone);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(HttpException error, String msg) {
                                                        Toast.makeText(RegActivity.this, "服务器忙，请稍候", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }
                                        } else {
                                            Toast.makeText(RegActivity.this, "验证失败或验证码不存在，请重新发送", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(com.lidroid.xutils.exception.HttpException error, String msg) {

                                }

                            });
                } else {
                    Tool.initToast(RegActivity.this, "请确保手机号和验证码正确");
                }

                break;
            case R.id.back:
                finish();
                break;
        }

    }

    // 快捷登陆
    private void ShortLog(String telephone, String password) {
        loginSerAccount(telephone, password);
    }

    private void sendMess() {
        SMSSDK.getSupportedCountries();
        SMSSDK.getVerificationCode("86", userPhone);
        Message_btn.setClickable(false);
        Message_btn.setBackgroundColor(Color.GRAY);
        Toast.makeText(RegActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Message_btn.setBackgroundResource(R.drawable.btn_default_small_normal_disable);
                Message_btn.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                Message_btn.setClickable(true);
                Message_btn.setBackgroundResource(R.drawable.btn_default_small_normal);
                Message_btn.setText("重新发送");
            }
        }.start();
        //进行获取验证码操作和倒计时1分钟操作
        //回调完成
//提交验证码成功
//返回支持发送验证码的国家列表
        eventHandler = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        //提交验证码成功
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
                Log.e("LoginActivity", "--00--" + event + "--01--" + result + "--02--" + data);
            }
        };
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    private void finishLogin(String name) {
        AVIMClient currentClient = AVIMClient.getInstance("android", "Mobile");
        currentClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                }
            }
        });
        Constants.USER_NAME = name;
        ChatManager chatManager = ChatManager.getInstance();
        String currentUserId = getCurrentUserId();
        chatManager.setupManagerWithUserId(currentUserId);
        chatManager.openClient(null);
        Intent intent = new Intent(RegActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (eventHandler != null) {
            SMSSDK.unregisterEventHandler(eventHandler);
        }
    }
}
