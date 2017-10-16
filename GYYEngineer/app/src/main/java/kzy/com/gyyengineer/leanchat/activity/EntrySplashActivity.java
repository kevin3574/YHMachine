package kzy.com.gyyengineer.leanchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
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

import cn.leancloud.chatkit.LCChatKit;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.engineer.activity.GuideActivity;
import kzy.com.gyyengineer.engineer.activity.LoginActivity;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.service.PushManager;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.PackageUtils;

public class EntrySplashActivity extends AVBaseActivity {
    public static final int SPLASH_DURATION = 2000;
    private static final int GO_MAIN_MSG = 1;
    private static final int GO_LOGIN_MSG = 2;
    private ChatManager chatManager = ChatManager.getInstance();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_MAIN_MSG:
                    imLogin();
                    break;
                case GO_LOGIN_MSG:
//          Intent intent = new Intent(EntrySplashActivity.this, EntryLoginActivity.class);
                    Intent intent = new Intent(EntrySplashActivity.this, LoginActivity.class);
                    EntrySplashActivity.this.startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    private boolean needGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_splash_layout);

        isNeedGuide();
    }

    private void initData() {
        if (LeanchatUser.getCurrentUser() != null) {
            String username = LeanchatUser.getCurrentUser().getUsername();
            String phone = username.substring(1, username.length());
            getforgetpwdinNet(phone);
        } else {
            handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
        }
    }

    private void imLogin() {
        LCChatKit.getInstance().open(LeanchatUser.getCurrentUserId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (filterException(e)) {
                    Intent intent = new Intent(EntrySplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void getforgetpwdinNet(final String phone) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("telephone", phone);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, Constants.URL + "cloundEngineer/userOldPassword", params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Log.e("EntrySplashActivity", "--------------result-------------" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = (String) jsonObject.get("status");
                    if (status.equals("1")) {
                        String forgetPwd = jsonObject.getString("password");
                        getLogData(phone, forgetPwd);
                    } else if (status.equals("0")) {
                        Toast.makeText(EntrySplashActivity.this, (String) jsonObject.get("data"), Toast.LENGTH_SHORT).show();
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
        String url = Constants.URL + "cloundEngineer/userLogin";
        RequestParams params = new RequestParams();
        params.addBodyParameter("telephone", phone);
        params.addBodyParameter("password", forgetPwd);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    Log.e("EntrySplashActivity", "--EntrySplashActivity-result----" + result);
                    if ("3".equals(status)) {
                        Log.e("EntrySplashActivity", "--3----" + result);
                        chatManager.closeWithCallback(new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient avimClient, AVIMException e) {
                            }
                        });
                        PushManager.getInstance().unsubscribeCurrentUserChannel();
                        AVUser.logOut();
                        PushManager.getInstance().unsubscribeCurrentUserChannel();
                        LeanchatUser.logOut();
                        startActivity(new Intent(EntrySplashActivity.this, LoginActivity.class));
                        finish();

                    } else if ("1".equals(status)) {
                        handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
                        Log.e("EntrySplashActivity", "--1----" + result);
                    } else {
                        handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
                        Log.e("EntrySplashActivity", "--else---" + result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    public void isNeedGuide() {
        SharedPreferences sp = getSharedPreferences("version", MODE_PRIVATE);

        //相当于旧版本
        final String version = sp.getString("version", null);

        //相当于新版本
        final String newVersion = PackageUtils.getPackageVersion(this);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //说明登录过，不需要进入导航页，直接进入主界面
                //当新版本与旧版本一致时直接跳转进入主界面
                if (newVersion.equals(version)) {

                    initData();

                } else {//需要进入导航页
                    Intent intent = new Intent(EntrySplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        thread.start();
    }
}
