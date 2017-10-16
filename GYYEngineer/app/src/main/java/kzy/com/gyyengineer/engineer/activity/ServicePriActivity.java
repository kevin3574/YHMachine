package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;

public class ServicePriActivity extends BaseActivity {
    private ImageView leftBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_pri);
        WebView wv_show = (WebView) findViewById(R.id.wv_show);

        WebSettings settings = wv_show.getSettings();
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);//support zoom
        settings.setUseWideViewPort(true);// 这个很关键
        settings.setLoadWithOverviewMode(true);

        wv_show.loadUrl("file:///android_asset/agreement.html");//// TODO: 2016/7/25 (服务协议)
        //自适应屏幕
        wv_show.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_show.getSettings().setLoadWithOverviewMode(true);
        leftBtn = (ImageView) findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
