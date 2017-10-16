package kzy.com.gyyengineer.leanchat.activity;

import android.os.Bundle;

import kzy.com.gyyengineer.R;

/**
 * Created by lzw on 14-9-24.
 */
public class ProfileNotifySettingActivity extends AVBaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_setting_notify_layout);
    setTitle(R.string.profile_notifySetting);
  }
}
