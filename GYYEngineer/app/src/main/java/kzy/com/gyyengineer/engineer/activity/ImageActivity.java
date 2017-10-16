package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;

import static kzy.com.gyyengineer.utils.PhotoUtils.avatarImageOption;

public class ImageActivity extends BaseActivity {

    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.activity_image)
    RelativeLayout activityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        inits(url);
    }

    private void inits(String url) {
        ImageLoader.getInstance().displayImage(url, iv, avatarImageOption);
        activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
