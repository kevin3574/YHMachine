package kzy.com.gyyengineer.engineer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.model.MarchineDataBean;
import kzy.com.gyyengineer.utils.MyHttpUtils;

public class MachineDetailsActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.tv_xzyl)
    TextView tvXzyl;
    @Bind(R.id.tv_jzyl)
    TextView tvJzyl;
    @Bind(R.id.tv_zxgy)
    TextView tvZxgy;
    @Bind(R.id.tv_yxsj)
    TextView tvYxsj;
    @Bind(R.id.tv_jzsj)
    TextView tvJzsj;
    @Bind(R.id.tv_klyc)
    TextView tvKlyc;
    @Bind(R.id.tv_ylyc)
    TextView tvYlyc;
    @Bind(R.id.tv_yfyc)
    TextView tvYfyc;
    @Bind(R.id.tv_hjwd)
    TextView tvHjwd;
    @Bind(R.id.tv_pqwd)
    TextView tvPqwd;
    @Bind(R.id.tv_zdjdl)
    TextView tvZdjdl;
    @Bind(R.id.tv_dy)
    TextView tvDy;
    private String cloudOrder;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    Log.e("MachineDetailsActivity", "----msg.obj----" + msg.obj);
                    MarchineDataBean marchineDataBean = (MarchineDataBean) msg.obj;
                    Log.e("MachineDetailsActivity", "----marchineDataBean----" + marchineDataBean);
                    tvXzyl.setText(marchineDataBean.getDischargePressure());
                    tvJzyl.setText(marchineDataBean.getLoadPressure());
                    tvZxgy.setText(marchineDataBean.getMiniPressure());
                    tvYxsj.setText(marchineDataBean.getRunTime());
                    tvJzsj.setText(marchineDataBean.getLoadTime());
                    tvKlyc.setText(marchineDataBean.getEmptyPressure());
                    tvYlyc.setText(marchineDataBean.getOilPressure());
                    tvYfyc.setText(marchineDataBean.getOilDivPressure());
                    tvHjwd.setText(marchineDataBean.getEnvironment());
                    tvPqwd.setText(marchineDataBean.getDischargeTemper());
                    tvZdjdl.setText(marchineDataBean.getElectricity());
                    tvDy.setText(marchineDataBean.getVoltage());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_details);
        ButterKnife.bind(this);
        cloudOrder = getIntent().getStringExtra("cloudOrder");
        Log.e("MachineDetailsActivity", "----cloudOrder----" + cloudOrder);
        initviews();
        initlisteners();
        initdatas();
    }

    private void initviews() {
        tvTop.setText("机器详情");
        btnTijao.setVisibility(View.GONE);
    }

    private void initlisteners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initdatas() {
        getDataInNet(cloudOrder);
    }

    private void getDataInNet(String cloudOrder) {
        if (cloudOrder == null) {
            return;
        }
        String url = Constants.URL + "cloundEngineer/marchineDetail";
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("cloudOrder", cloudOrder);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Log.e("MachineDetailsActivity", "--------" + result);
                Gson gson = new Gson();
                MarchineDataBean currentDeteilBean = gson.fromJson(result, MarchineDataBean.class);
                Message message = new Message();
                message.what = 10;
                message.obj = currentDeteilBean;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
}
