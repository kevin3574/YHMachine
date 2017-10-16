package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.service.PushManager;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private static final int RESULTCODE_LOGOUT_FINISH_OK = 88;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    private ChatManager chatManager = ChatManager.getInstance();
    private LinearLayout account;
    private static final int MAKE_PED_REQUEST = 11;
    private static final int MAKE_PWD_RESULT = 10;
    private static final int MAKE_PWD_RESULT_OK = 12;  // 修改密码成功 返回结果码
    private TextView profile_logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initviews();
        initlisteners();
    }

    private void initviews() {
        account = (LinearLayout) findViewById(R.id.account);
        profile_logout_btn = (TextView) findViewById(R.id.profile_logout_btn);
        tvTop.setText("设置");
        btnTijao.setVisibility(View.GONE);
    }

    private void initlisteners() {
        profile_logout_btn.setOnClickListener(this);
        account.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account:
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, ChangePwdActivity.class);
                startActivityForResult(intent, MAKE_PED_REQUEST);
                break;
            case R.id.profile_logout_btn:
                chatManager.closeWithCallback(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                    }
                });
                PushManager.getInstance().unsubscribeCurrentUserChannel();
                AVUser.logOut();
                PushManager.getInstance().unsubscribeCurrentUserChannel();
                LeanchatUser.logOut();
                setResult(RESULTCODE_LOGOUT_FINISH_OK);
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAKE_PED_REQUEST && resultCode == MAKE_PWD_RESULT) {
            SettingActivity.this.setResult(MAKE_PWD_RESULT_OK);
            finish();
        }
    }
}
