package kzy.com.gyyengineer.engineer.activity;

import android.app.Activity;
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
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.service.PushManager;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.Result;
import kzy.com.gyyengineer.model.UserInfo;
import kzy.com.gyyengineer.model.UserReg;
import kzy.com.gyyengineer.utils.MyAndroidUtil;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.Tool;

/*
密码校验页面
 */

public class CheckActivity extends Activity {
    private static final int FORGET_PWD_RESULT_OK = 100;
    private EditText register_password, register_password_again;
    private Button confirm_btn;
    private ImageView btn_back;
    private String pass;
    private String flag;
    private String forgetPwd;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        phone = intent.getStringExtra("phone");
        init();

    }

    private void init() {
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        btn_back = (ImageView) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register_password = (EditText) findViewById(R.id.register_password);
        register_password_again = (EditText) findViewById(R.id.register_password_again);
        getforgetpwdinNet();    //获取原始密码
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = register_password.getText().toString();
                String pass_again = register_password_again.getText().toString();
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(CheckActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(CheckActivity.this, "请输入6位或者6位以上密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass_again)) {
                    Toast.makeText(CheckActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(pass_again)) {
                    Toast.makeText(CheckActivity.this, "输入密码不一致，请重新确认", Toast.LENGTH_SHORT).show();
                } else if (forgetPwd != null) {
                    Log.e("CheckActivity", "--04--" + phone + "--forgetPwd--" + forgetPwd);
                    LeanchatUser.logInInBackground("D" + phone, forgetPwd,
                            new LogInCallback<LeanchatUser>() {
                                @Override
                                public void done(LeanchatUser avUser, AVException e) {
                                    if (e == null) {
                                        final AVUser aUser = AVUser.getCurrentUser();
                                        aUser.get("property");
                                        if (aUser.get("property").equals("engineer")) {
                                            ChatManager chatManager = ChatManager.getInstance();
                                            chatManager.setupManagerWithUserId(AVUser.getCurrentUser().getObjectId());
                                            chatManager.openClient(null);
                                            Log.e("CheckActivity", "--05--" + forgetPwd + "--pass--" + pass);
                                            aUser.updatePasswordInBackground(forgetPwd, pass,
                                                    new UpdatePasswordCallback() {

                                                        @Override
                                                        public void done(AVException arg0) {
                                                            if (arg0 == null) {
                                                                String utr = Constants.URL + "cloundEngineer/userPassword";
                                                                UserInfo user = new UserInfo();
                                                                user.setPhone(phone);
                                                                user.setPass(pass);
                                                                MyHttpUtils.handData(handler, 17, utr, user);
                                                            } else {
                                                                Tool.initToast(getApplicationContext(),
                                                                        "设置密码失败");
                                                                Log.e("CheckActivity", "--06--" + arg0);
                                                            }
                                                        }
                                                    });

                                        } else {
                                            ChatManager chatManager = ChatManager.getInstance();
                                            chatManager.closeWithCallback(new AVIMClientCallback() {
                                                @Override
                                                public void done(AVIMClient avimClient, AVIMException e) {
                                                }
                                            });
                                            PushManager.getInstance().unsubscribeCurrentUserChannel();
                                            AVUser.logOut();
                                            Tool.initToast(CheckActivity.this,
                                                    getResources().getString(R.string.login_error));
                                        }
                                    } else {
                                        Tool.initToast(CheckActivity.this, "leancloud登录失败" + e);
                                        Log.e("CheckActivity", "--03--" + e);
                                    }
                                }
                            }, LeanchatUser.class);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    UserReg userReg = (UserReg) msg.obj;
                    if (userReg.getStatus().equals("1")) {
                        MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT,
                                userReg.getTelephone());
                        Intent intent = new Intent(CheckActivity.this,
                                LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Utils.toast("注册成功，请登录");
                        finish();
                    } else {
                        Utils.toast(App.getInstance().getString(
                                R.string.registerFailed)
                        );
                    }
                    break;

                case 17:
                    Log.e("CheckActivity", "--msg.obj--" + msg.obj);
                    Result result = (Result) msg.obj;
                    Toast.makeText(CheckActivity.this, result.getData(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                    CheckActivity.this.setResult(FORGET_PWD_RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    private void creaSerAccount(String userName, String passWord) {
        String url = Constants.URL + "cloudUser/userPassword";
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(userName);
        userInfo.setPass(passWord);
        MyHttpUtils.handData(handler, 11, url, userInfo);
    }

    private void createAccount(final String userName, final String passWord) {
        LeanchatUser.signUpByNameAndPwdAndProperty(userName, passWord, "engineer", new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    Utils.toast(App.getInstance().getString(
                            R.string.registerFailed)
                            + e.getMessage());
                } else {
                    creaSerAccount(phone, pass);
                }
            }
        });
    }

    public void getforgetpwdinNet() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("telephone", phone);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, Constants.URL + "cloundEngineer/userOldPassword", params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = (String) jsonObject.get("status");
                    if (status.equals("1")) {
                        forgetPwd = (String) jsonObject.get("password");
                        getLogData(phone, forgetPwd);
                    } else if (status.equals("0")) {
                        Toast.makeText(CheckActivity.this, (String) jsonObject.get("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("", "获取原始密码成功" + result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("", "获取原始密码失败" + msg);
            }
        });
    }

    private void getLogData(String phone, String forgetPwd) {
        RequestParams requestParams = new RequestParams();

    }
}
