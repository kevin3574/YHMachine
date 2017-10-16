package kzy.com.gyyengineer.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.adapter.OtherGoodSayLvAdapter;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.OtherGoodSayBean;
import kzy.com.gyyengineer.model.TiUser;
import kzy.com.gyyengineer.utils.MyHttpUtils;

import static kzy.com.gyyengineer.utils.PhotoUtils.avatarImageOption;

public class GoodSeyActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    private TextView tvMyhandPercent;
    private TextView tvMyname;
    private TextView tvMyRank;
    private TextView tvMyPercent;
    private ImageView ivMyHandView;
    private View lvHandView;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_sey);
        lv = (ListView) findViewById(R.id.lv);
        ButterKnife.bind(this);
        initViews();
        initListeners();
        initdatas();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 267:
                    OtherGoodSayBean dataBean = (OtherGoodSayBean) msg.obj;
                    Log.e("GoodSeyActivity", "--dataBean--" + dataBean);
                    final List<OtherGoodSayBean.DataBean> dataBeanList = dataBean.getData();
                    lv.setAdapter(new OtherGoodSayLvAdapter(GoodSeyActivity.this, dataBeanList));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("GoodSeyActivity", "--position--" + position);
                            if (position == 0) {
                                // 点击了我的条目
                                String username = LeanchatUser.getCurrentUser().getUsername();
                                String phone = username.substring(1, username.length());
                                if (phone != null) {
                                    Intent intent = new Intent(GoodSeyActivity.this, GoodSayDetailsActivity.class);
                                    intent.putExtra("telephone", phone);
                                    startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(GoodSeyActivity.this, GoodSayDetailsActivity.class);
                                OtherGoodSayBean.DataBean bean = dataBeanList.get(position - 1);
                                intent.putExtra("telephone", bean.getTelephone());
                                startActivity(intent);
                            }

                        }
                    });
                    break;
            }
        }
    };

    private void initViews() {
        lvHandView = View.inflate(this, R.layout.goodsayactivity_handview, null);
        tvMyhandPercent = (TextView) this.lvHandView.findViewById(R.id.tv_myhandPercent);
        tvMyname = (TextView) this.lvHandView.findViewById(R.id.tv_myname);
        tvMyRank = (TextView) this.lvHandView.findViewById(R.id.tv_myRank);
        tvMyPercent = (TextView) this.lvHandView.findViewById(R.id.tv_myPercent);
        ivMyHandView = (ImageView) this.lvHandView.findViewById(R.id.iv_myHandView);
        btnTijao.setVisibility(View.GONE);
        tvTop.setText("好评率");
        lv.addHeaderView(this.lvHandView);

    }

    private void initListeners() {
        back.setOnClickListener(this);
    }

    private void initdatas() {
        getMyDate();
        getOtherData();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public void getMyDate() {
        String url = Constants.URL + "cloundEngineer/myPraise";
        RequestParams params = new RequestParams();
        String username = LeanchatUser.getCurrentUser().getUsername();
        params.addBodyParameter("telephone", username.substring(1, username.length()));
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Log.e("GoodSeyActivity", "--result--" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    tvMyhandPercent.setText(jsonObject.getString("rate")+"%");
                    tvMyname.setText(jsonObject.getString("engineer"));
                    tvMyRank.setText("第" + jsonObject.getString("rank") + "名");
                    tvMyPercent.setText(jsonObject.getString("rate")+"%");
                    ImageLoader.getInstance().displayImage(jsonObject.getString("engineerImage"), ivMyHandView, avatarImageOption);
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

    public void getOtherData() {
        String url = Constants.URL + "cloundEngineer/rankPraise";
        String username = LeanchatUser.getCurrentUser().getUsername();
        String phone = username.substring(1, username.length());
        TiUser tiUser = new TiUser();
        tiUser.setCardId(phone);
        MyHttpUtils.handData(handler, 267, url, tiUser);
    }
}
