package kzy.com.gyyengineer.Base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.utils.HideSoftInputHelperTool;
import kzy.com.gyyengineer.utils.Tool;

/**
 * 项目名称：DoctorMhealth
 * 类描述：基类
 * 创建人：赵金祥
 * 邮箱：cc112837@163.com
 * 创建时间：2016/5/10 11:10
 * 修改人：Administrator
 * 修改时间：2016/5/10 11:10
 * 修改备注：
 */
public class BaseActivity extends AppCompatActivity {

    public ImageView leftButton, rightButton;
    TextView titleTextView;
    FrameLayout titleFrameLayout;

    public void initTitle(){

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        titleTextView = (TextView) findViewById(R.id.titleView);
        titleFrameLayout = (FrameLayout)findViewById(R.id.titlebar);
        leftButton = (ImageView) findViewById(R.id.leftBtn);
        rightButton = (ImageView) findViewById(R.id.rightBtn);
        if(leftButton!=null)
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    protected void setTitleVisibility(boolean isVisibility) {
        if (isVisibility) {
            titleFrameLayout.setVisibility(View.VISIBLE);
        } else {
            titleFrameLayout.setVisibility(View.GONE);
        }

    }

    // If the left button is set to false to hide
    protected void setLeftButtonVisibility(boolean isVisibility) {
        if (isVisibility) {
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }

    }

    /** If the right button is set to false to hide */
    protected void setRightButtonVisibility(boolean isVisibility) {
        if (isVisibility) {
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }


    protected ProgressDialog showSpinnerDialog() {
        //activity = modifyDialogContext(activity);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setMessage(getString(R.string.chat_utils_hardLoading));
        if (!isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    protected void setTitleString(String title) {
        if (titleTextView!=null) {
            titleTextView.setText(title);
        }
    }

    protected void setRightButtonDrawable(int drawable) {
        rightButton.setBackgroundResource(drawable);
    }

    /** 输入框清 */
    protected void editTextClean(final EditText ed, final ImageView deteleImageView) {
        ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    deteleImageView.setVisibility(View.GONE);
                } else {
                    deteleImageView.setVisibility(View.VISIBLE);
                    deteleImageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            ed.setText("");

                        }

                    });
                }

            }

        });
    }

    // 键盘处理 点击空白页面关闭键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        HideSoftInputHelperTool.hide(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    private String getMatcherResource(String activityName) {
        String resourceName;
        if (activityName.contains("Activity")) {
            resourceName = activityName.substring(0, activityName.indexOf("Activity"));
        } else {
            resourceName = activityName;
        }
        resourceName = Tool.tofirstLowerCase(resourceName);
        Pattern p = Pattern.compile("\\p{Upper}");
        Matcher m = p.matcher(resourceName);
        while (m.find()) {
            String s = m.group();
            resourceName = resourceName.replace(s, "_" + s.toLowerCase());
        }

        return resourceName;
    }

    public Bitmap locationImage(int drawable) {
        return new BitmapDrawable(getResources().openRawResource(drawable)).getBitmap();
    }

    public String getDay() {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 格式大小写有区别
        return fmt.format(rightNow.getTime());

    }

    protected void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected void toast(Exception e){
        if(e!=null){
            toast(e.getMessage());
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.to_right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
    }
}

