package kzy.com.gyyengineer.engineer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.utils.LCIMConstants;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.adapter.MachineDetailAdapter;
import kzy.com.gyyengineer.adapter.PictureAdapter;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.leanchat.activity.AVBaseActivity;
import kzy.com.gyyengineer.leanchat.activity.ChatRoomActivity;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.CurrentDeteilBean;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.Util;

import static kzy.com.gyyengineer.leanchat.model.LeanchatUser.getCurrentUser;

public class DetailOneActivity extends AVBaseActivity implements View.OnClickListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_top)
    TextView tvTop;
    @Bind(R.id.btn_tijao)
    Button btnTijao;
    @Bind(R.id.ll_top)
    RelativeLayout llTop;
    @Bind(R.id.tv_jqbh)
    TextView tvJqbh;
    @Bind(R.id.tv_jqpp)
    TextView tvJqpp;
    @Bind(R.id.tv_jqlx)
    TextView tvJqlx;
    @Bind(R.id.tv_gzms)
    TextView tvGzms;
    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.activity_detail_one)
    LinearLayout activityDetailOne;
    @Bind(R.id.but_ok)
    Button but_ok;
    @Bind(R.id.tv_bxr)
    TextView tvBxr;
    @Bind(R.id.tv_dh)
    TextView tvDh;
    @Bind(R.id.tv_dz)
    TextView tvDz;
    @Bind(R.id.ll_engineerMsg)
    LinearLayout llEngineerMsg;
    @Bind(R.id.lx)
    TextView lx;
    @Bind(R.id.hj)
    TextView hj;
    @Bind(R.id.dh)
    TextView dh;
    private String cloudOrder;
    CurrentDeteilBean currentDeteilBean;
    private PictureAdapter pictureAdapter;
    private boolean isRepair;
    @Bind(R.id.lv)
    ListView lv;

    private int SpotActivity_FINISH = 001;  // 请求码  当未解决页面关闭时候执行
    private int SpotActivity_FINISH_RESULT = 011; // 结果码  当未解决页面关闭时候执行
    private int PhoningActivity_FINISH_RESULT = 201; //  结果码   运行状态界面提交之后返回
    private String userId;
    Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    currentDeteilBean = (CurrentDeteilBean) msg.obj;
                    CurrentDeteilBean.RepairDataBean repairDataBean = currentDeteilBean.getRepairData().get(0);
                    List<String> pictures = repairDataBean.getPicture();  // 故障图片
                    tvJqbh.setText(repairDataBean.getCloudOrder());
                    tvJqpp.setText(repairDataBean.getMachineBrand());
                    tvJqlx.setText(repairDataBean.getMarchineType());
                    tvGzms.setText(repairDataBean.getFaultDescription());
                    if (pictures.size() != 0) {
                        pictureAdapter = new PictureAdapter(pictures, DetailOneActivity.this);
                        gv.setAdapter(pictureAdapter);
                    }
                    tvBxr.setText(repairDataBean.getUserName());
                    tvDz.setText(repairDataBean.getCloudAddress());
                    tvDh.setText(repairDataBean.getTelephone());
                    userId = repairDataBean.getUserId();
                    lv.setAdapter(new MachineDetailAdapter(DetailOneActivity.this, currentDeteilBean));
                    break;
            }

        }
    };
    private String name;
    private String status;
    private List<CurrentDeteilBean.RecordDataBean> recordDatas;
    private String statu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_one);
        ButterKnife.bind(this);

        isRepair = false;
        cloudOrder = getIntent().getStringExtra("cloudOrder");
        status = getIntent().getStringExtra("status");
        statu = getIntent().getStringExtra("statu");
        name = getCurrentUser().getUsername();
        name = name.substring(1, name.length());
        //  initdaohang();
        initViews();
        initListeners();
        initDatas();

    }

    private void initViews() {
        tvTop.setText("维修单详情");
        btnTijao.setVisibility(View.GONE);
        llEngineerMsg.setVisibility(View.GONE);
    }

    private void initListeners() {
        but_ok.setOnClickListener(this);
        back.setOnClickListener(this);
        btnTijao.setOnClickListener(this);
        hj.setOnClickListener(this);
        lx.setOnClickListener(this);
        dh.setOnClickListener(this);
        if ("0".equals(status)) {
            but_ok.setBackgroundResource(R.color.bg_gray);
            but_ok.setText("确认接单");
        }
        if ("0".equals(statu)) {
            but_ok.setText("确认接单");
            isRepair = false;
        } else if ("1".equals(statu)) {
            but_ok.setText("维修完成");
            isRepair = true;
        }

    }

    private void initDatas() {

        initCreateData(cloudOrder);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = pictureAdapter.getItem(position);
                Intent intent = new Intent(DetailOneActivity.this, ImageActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetailOneActivity.this, MachineDetailsActivity.class);
                intent.putExtra("cloudOrder", cloudOrder);
                startActivity(intent);
            }
        });
    }

    private void initCreateData(String cloudOrder) {
        if (cloudOrder == null) {
            return;
        }
        String url = Constants.URL + "cloundEngineer/repairDetail";
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("cloudOrder", cloudOrder);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Gson gson = new Gson();
                CurrentDeteilBean currentDeteilBean = gson.fromJson(result, CurrentDeteilBean.class);
                Message message = new Message();
                message.what = 1;
                message.obj = currentDeteilBean;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Utils.toast("网络异常，请稍后重试");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                DetailOneActivity.this.finish();
                break;

            case R.id.but_ok:
                if ("0".equals(status)) {
                    Utils.toast("对不起，您已接过单");
                } else {
                    if (!isRepair) {
                        sbuData2Net(cloudOrder, name);
                    } else {
                        String jqbh = tvJqbh.getText().toString();
                        Intent phoningintent = new Intent(DetailOneActivity.this, PhoningActivity.class);
                        phoningintent.putExtra("jqbh", jqbh);
                        phoningintent.putExtra("cloudOrder", cloudOrder);
                        startActivityForResult(phoningintent, SpotActivity_FINISH);
                    }
                }

                break;
            case R.id.hj:

                callPhone();

                break;
            case R.id.lx:
                Log.e("DetailOneActivity", "--点了联系--" + userId);
                if (userId != null) {
                    Intent intent1 = new Intent(DetailOneActivity.this, ChatRoomActivity.class);
                    intent1.putExtra(LCIMConstants.PEER_ID, userId);
                    startActivity(intent1);
                }
                break;

        }
    }

    public LocationClient locClient;
    // 外拨电话
    private String[] perms = {Manifest.permission.CALL_PHONE};
    private final int PERMS_REQUEST_CODE = 200;

    //拨打电话
    private void callPhone() {
        //检查拨打电话权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + tvDh.getText().toString().trim()));
            startActivity(intent);
        }
    }

    private void sbuData2Net(String cloudOrder, String name) {
        String url = Constants.URL + "cloundEngineer/repaird";
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("cloudOrder", cloudOrder);
        requestParams.addBodyParameter("telephone", name);
        MyHttpUtils.sendData(HttpRequest.HttpMethod.POST, url, requestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String status = (String) jsonObject.get("status");
                    if ("1".equals(status)) {
                        Util.showToast(DetailOneActivity.this, (String) jsonObject.get("data"));
                        llEngineerMsg.setVisibility(View.VISIBLE);
                        but_ok.setText("维修完成");
                        isRepair = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    String status = (String) jsonObject.get("status");
                    if ("0".equals(status)) {
                        Util.showToast(DetailOneActivity.this, (String) jsonObject.get("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SpotActivity_FINISH && resultCode == SpotActivity_FINISH_RESULT) {
            finish();
        }
        if (requestCode == SpotActivity_FINISH && resultCode == PhoningActivity_FINISH_RESULT) {
            finish();
        }
    }


}
