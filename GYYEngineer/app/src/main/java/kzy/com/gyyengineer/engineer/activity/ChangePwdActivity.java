package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.service.PushManager;
import kzy.com.gyyengineer.model.Result;
import kzy.com.gyyengineer.model.UserInfo;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.Tool;

public class ChangePwdActivity extends BaseActivity {
    private static final int MAKE_PWD_RESULT = 10;
    private static final int FINISH_CHANGEPWDACT_RESULT = 210;
    EditText oldPwdView, pwdView, pwdView1;
    Button subBtn;
    ImageView btn_back;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 27:
                    Log.e("ChangePwdActivity", "--修改密码msg--" + msg);
                    Result result = (Result) msg.obj;
                    Log.e("ChangePwdActivity", "--修改密码result--" + result);
                    if (result.getStatus().equals("1")) {
                        Tool.initToast(getApplicationContext(),
                                "密码修改成功");
                        ChangePwdActivity.this.setResult(MAKE_PWD_RESULT);
                        finish();

                    } else {
                        Tool.initToast(getApplicationContext(),
                                "密码修改失败");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_pwd);
        oldPwdView = (EditText) findViewById(R.id.oldPwdView);
        pwdView = (EditText) findViewById(R.id.pwdView);
        pwdView1 = (EditText) findViewById(R.id.pwdView1);
        subBtn = (Button) findViewById(R.id.subBtn);
        btn_back = (ImageView) findViewById(R.id.leftBtn);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePwdActivity.this.setResult(FINISH_CHANGEPWDACT_RESULT);
                finish();
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(oldPwdView.getText().toString())) {
                    Tool.initToast(getApplicationContext(), "请输入旧密码");
                } else if (!TextUtils.isEmpty(oldPwdView.getText().toString()) && oldPwdView.getText().toString().length() < 6) {
                    Tool.initToast(getApplicationContext(), "旧密码不能少于6位");
                } else if (TextUtils.isEmpty(pwdView.getText().toString())) {
                    Tool.initToast(getApplicationContext(), "请输入新密码");
                } else if (!TextUtils.isEmpty(pwdView.getText().toString()) && pwdView.getText().toString().length() < 6) {
                    Tool.initToast(getApplicationContext(), "新密码不能少于6位");
                } else if (TextUtils.isEmpty(pwdView1.getText().toString())) {
                    Tool.initToast(getApplicationContext(), "请再次输入新密码");
                } else if (!pwdView.getText().toString()
                        .equals(pwdView1.getText().toString())) {
                    Tool.initToast(getApplicationContext(), "请确保两次密码一致");
                } else {

                    String username = LeanchatUser.getCurrentUser().getUsername();
                    final String phone = username.substring(1, username.length());
                    Log.e("ChangePwdActivity", "--01--" + phone + "--02--" + oldPwdView.getText()
                            .toString());
                    LeanchatUser.logInInBackground("D" + phone, oldPwdView.getText()
                                    .toString(),
                            new LogInCallback<LeanchatUser>() {
                                @Override
                                public void done(LeanchatUser avUser, AVException e) {
                                    if (e == null) {
                                        Log.e("ChangePwdActivity", "--密码重置进来了--");
                                        final AVUser aUser = AVUser.getCurrentUser();
                                        aUser.get("property");
                                        if (aUser.get("property").equals("engineer")) {

                                            ChatManager chatManager = ChatManager.getInstance();
                                            chatManager.setupManagerWithUserId(AVUser.getCurrentUser().getObjectId());
                                            Log.e("ChangePwdActivity", "用户ID-----" + AVUser.getCurrentUser().getObjectId());
                                            chatManager.openClient(null);
                                            aUser.updatePasswordInBackground(oldPwdView.getText()
                                                            .toString(), pwdView.getText().toString(),
                                                    new UpdatePasswordCallback() {

                                                        @Override
                                                        public void done(AVException arg0) {
                                                            if (arg0 == null) {
                                                                String utr = Constants.URL + "cloundEngineer/userPassword";
                                                                UserInfo user = new UserInfo();
                                                                user.setPhone(phone);
                                                                user.setPass(pwdView.getText().toString() + "");
                                                                user.setOldpwd(oldPwdView.getText()
                                                                        .toString());
                                                                MyHttpUtils.handData(handler, 27, utr, user);
                                                            } else
                                                                Tool.initToast(getApplicationContext(),
                                                                        "设置密码失败");
                                                            Log.e("ChangePwdActivity", "--修改密码失败--" + arg0);
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
                                            Tool.initToast(ChangePwdActivity.this,
                                                    getResources().getString(R.string.login_error));
                                        }
                                    } else {
                                        Tool.initToast(ChangePwdActivity.this, "leancloud设置密码失败" + e);
                                        Log.e("ChangePwdActivity", "--03--" + e);
                                    }
                                }
                            }, LeanchatUser.class);
                }
            }
        });
    }
}
