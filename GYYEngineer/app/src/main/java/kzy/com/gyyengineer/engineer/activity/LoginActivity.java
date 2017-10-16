package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.activity.MainActivity;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.service.PushManager;
import kzy.com.gyyengineer.leanchat.util.PhotoUtils;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.UserInfo;
import kzy.com.gyyengineer.model.UserReg;
import kzy.com.gyyengineer.utils.MyAndroidUtil;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.PreferenceUtils;
import kzy.com.gyyengineer.utils.Tool;
import kzy.com.gyyengineer.utils.Util;

import static kzy.com.gyyengineer.leanchat.model.LeanchatUser.getCurrentUser;

public class LoginActivity extends BaseActivity implements TextWatcher {
    private static final int SHORTLOG_REQUEST = 100;
    Button loginBtn, regButton, forgetButton;
    TextView nameText, pwdText;
    private String name, pwd;
    ImageView headicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        headicon = (ImageView) findViewById(R.id.headicon);
        Log.e("LoginActivity", "-00-" + ImageLoader.getInstance() + "_01_" + App.sharedPreferences.getString(Constants.icon, null) + "-02-" + PhotoUtils.avatarlogin);
        ImageLoader.getInstance().displayImage(App.sharedPreferences.getString(Constants.icon, null), headicon,
                PhotoUtils.avatarlogin);
        if (getCurrentUser() != null) {
            name = getCurrentUser().getUsername();
            finishLogin();
        }
        initTitle();
        forgetButton = (Button) findViewById(R.id.forgetButton);
        nameText = (TextView) findViewById(R.id.nameText);
        pwdText = (TextView) findViewById(R.id.pwdText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        regButton = (Button) findViewById(R.id.regButton);
        nameText.addTextChangedListener(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = nameText.getText().toString();
                pwd = pwdText.getText().toString();
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                intent.putExtra("flag", "for");
                startActivity(intent);
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                //    Intent intent = new Intent(LoginActivity.this, UserMachineActivity.class);
                intent.putExtra("flag", "reg");
                startActivity(intent);
            }
        });

        // 已登录过,自动登录
        name = App.sharedPreferences.getString(Constants.LOGIN_ACCOUNT,
                null);
        pwd = App.sharedPreferences.getString(Constants.LOGIN_PWD, null);
        if (name != null)
            nameText.setText(name);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    boolean mobileNumber = Util.getInstance().isMobileNumber(name);
                    if (mobileNumber) {
                        if (!TextUtils.isEmpty(pwd)) {
                            if (pwd.length() < 6) {
                                Utils.toast("密码最少6位");
                            } else {
                                loginSerAccount(name, pwd);
                                Log.e("LoginActivity", "-----name----" + name + "---pwd---" + pwd);
                            }
                        } else {
                            Utils.toast("请输入密码");
                        }

                    } else {
                        Utils.toast("请输入正确的手机号");
                    }
                    break;
                case 12:
                    UserReg userReg = (UserReg) msg.obj;
                    Log.e("LoginActivity", "-----userReg----" + userReg);
                    if (userReg.getStatus().equals("1")) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        if (Integer.valueOf(userReg.getStatus()) == 1) {
                            //   MyAndroidUtil.editXmlByString(Constants.LOGIN_CHECK, Integer.valueOf(userReg.getStatus()) + "");
                            PreferenceUtils.setPrefString(getApplicationContext(), "phone", name);
                            finishLogin();
                        }
                    } else if (userReg.getStatus().equals("0")) {
                        Toast.makeText(LoginActivity.this, userReg.getData(), Toast.LENGTH_LONG).show();
                    } else if (userReg.getStatus().equals("2")) {
                        Toast.makeText(LoginActivity.this, userReg.getData(), Toast.LENGTH_LONG).show();
                    } else if (userReg.getStatus().equals("3")) {
                        Toast.makeText(LoginActivity.this, userReg.getData(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case 404:
                    Utils.toast("网络异常，请稍后重试");
                    break;
            }
        }
    };

    private void loginSerAccount(final String name, final String pwd) {
        final String url = Constants.URL + "cloundEngineer/userLogin";
        LeanchatUser.logInInBackground("D" + name, pwd,
                new LogInCallback<LeanchatUser>() {
                    @Override
                    public void done(LeanchatUser avUser, AVException e) {
                        if (e == null) {
                            AVUser aUser = AVUser.getCurrentUser();

                            aUser.get("property");
                            if (aUser.get("property").equals("engineer")) {
                                // 第二个参数：登录标记 Tag
                                String currentUserId = getCurrentUserId();
                                UserInfo userInfo = new UserInfo();
                                userInfo.setPhone(name);
                                userInfo.setPass(pwd);
                                userInfo.setObjectId(currentUserId);
                                getCurrentUser().setMobilePhoneNumber("D" + name);
                                MyHttpUtils.handData(handler, 12, url, userInfo);
                            } else {
                                ChatManager chatManager = ChatManager.getInstance();
                                chatManager.closeWithCallback(new AVIMClientCallback() {
                                    @Override
                                    public void done(AVIMClient avimClient, AVIMException e) {
                                    }
                                });
                                PushManager.getInstance().unsubscribeCurrentUserChannel();
                                AVUser.logOut();
                                Tool.initToast(LoginActivity.this,
                                        getResources().getString(R.string.login_error));
                            }

                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(e.getMessage());
                                String code = jsonObject.getString("code");
                                if ("210".equals(code)) {
                                    Utils.toast("密码错误");
                                } else if ("211".equals(code)) {
                                    Utils.toast("还未注册,请注册");
                                } else if ("219".equals(code)) {
                                    Utils.toast("登录失败次数超过限制，请稍候再试");
                                }
                            } catch (JSONException e1) {
                            }
                            Log.e("LoginActivity", "-----登录失败----" + e.getMessage());
                        }
                    }
                }, LeanchatUser.class);

    }

    private static String getCurrentUserId() {
        LeanchatUser currentUser = getCurrentUser(LeanchatUser.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }

    private void finishLogin() {
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
        chatManager.setupManagerWithUserId(AVUser.getCurrentUser().getObjectId());
        String objectId = AVUser.getCurrentUser().getObjectId();
        Log.e("LoginActivity", "-objectId-" + objectId);
        chatManager.openClient(null);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        name = App.sharedPreferences.getString(Constants.LOGIN_ACCOUNT,
                null);
        if (name != null)
            nameText.setText(name);
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT, nameText.getText()
                .toString());
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT, nameText.getText()
                .toString());

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT, nameText.getText()
                .toString());
    }

    public void ShortLog(View view) {
        Intent intent = new Intent(LoginActivity.this, RegActivity.class);
        intent.putExtra("flag", "short_log");
        startActivityForResult(intent, SHORTLOG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHORTLOG_REQUEST && resultCode == SHORTLOG_REQUEST) {
            finish();
        }
    }
}
