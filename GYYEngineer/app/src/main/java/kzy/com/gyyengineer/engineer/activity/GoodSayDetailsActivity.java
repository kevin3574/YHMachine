package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.utils.MyHttpUtils;

import static kzy.com.gyyengineer.utils.PhotoUtils.avatarImageOption;

public class GoodSayDetailsActivity extends AppCompatActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.iv_handView)
    ImageView ivHandView;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_percent)
    TextView tvPercent;
    @Bind(R.id.tv_dqmc)
    TextView tvDqmc;
    @Bind(R.id.tv_zds)
    TextView tvZds;
    @Bind(R.id.tv_hps)
    TextView tvHps;
    @Bind(R.id.tv_zps)
    TextView tvZps;
    @Bind(R.id.tv_cps)
    TextView tvCps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_say_details);
        ButterKnife.bind(this);
        String telephone = getIntent().getStringExtra("telephone");
        if (telephone != null) {
            initDatas(telephone);
        }
        initviews();
        initListeners();
    }

    private void initviews() {
        tvTop.setText("好评率详情");
        btnTijao.setVisibility(View.GONE);
        tvNum.setVisibility(View.INVISIBLE);
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas(String telephone) {
        String url = Constants.URL + "cloundEngineer/myPraise";
        RequestParams params = new RequestParams();
        params.addBodyParameter("telephone", telephone);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String engineer = jsonObject.getString("engineer");
                    if (TextUtils.isEmpty(engineer)) {
                        tvName.setText("");
                    } else {
                        tvName.setText(engineer);
                    }

                    tvPercent.setText(jsonObject.getString("rate") + "%");
                    tvDqmc.setText(jsonObject.getString("rank"));
                    tvZds.setText(jsonObject.getString("totalNum"));
                    tvHps.setText(jsonObject.getString("goodPraise"));
                    tvZps.setText(jsonObject.getString("praise"));
                    tvCps.setText(jsonObject.getString("badPraise"));
                    ImageLoader.getInstance().displayImage(jsonObject.getString("engineerImage"), ivHandView, avatarImageOption);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.toast("网络异常，请稍后重试");
            }
        });
    }
}
