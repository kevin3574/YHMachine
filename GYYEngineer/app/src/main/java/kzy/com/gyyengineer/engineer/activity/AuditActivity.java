package kzy.com.gyyengineer.engineer.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.Base.BaseActivity;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.jpush.ExampleUtil;
import kzy.com.gyyengineer.jpush.TagAliasOperatorHelper;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.service.PushManager;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.AuditBean;
import kzy.com.gyyengineer.model.ImageItem;
import kzy.com.gyyengineer.model.UserReg;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.view.DialogListener;
import kzy.com.gyyengineer.view.TvDialog;

import static kzy.com.gyyengineer.jpush.TagAliasOperatorHelper.ACTION_SET;
import static kzy.com.gyyengineer.jpush.TagAliasOperatorHelper.sequence;

/**
 * 创建人：赵金祥
 * 工程师审核页面
 */
public class AuditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private Button btn_tijao;
    private TextView tv_top;
    private EditText et_name;
    private EditText et_namenum1;
    private EditText et_password;
    private EditText et_twoPssword;
    private GridView gridView1;
    private Button et_submit;
    private Bitmap bmp;
    private SimpleAdapter simpleAdapter;
    private List imageItem;
    private List<ImageItem> listitem = new ArrayList<>();
    private Cursor cursor;
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;
    private ProgressDialog pdialog;
    private ChatManager chatManager = ChatManager.getInstance();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 39:
                    Log.e("AuditActivity", "-成功了-" + msg.obj);
                    if (pdialog != null) {
                        pdialog.dismiss();
                    }
                    final UserReg stepInfo = (UserReg) msg.obj;
                    if ("1".equals(stepInfo.getStatus())) {


                        LeanchatUser.signUpByNameAndPwdAndProperty("D" + userphone, et_twoPssword.getText().toString(), "engineer", new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e != null) {
                                    Utils.toast(App.getInstance().getString(
                                            R.string.registerFailed));
                                    Log.e("LoginActivity", "----注册失败00---" + e.getMessage());
                                } else {
                                    setjPushTag(userphone);
                                    Toast.makeText(AuditActivity.this, stepInfo.getData(), Toast.LENGTH_LONG).show();
                                    chatManager.closeWithCallback(new AVIMClientCallback() {
                                        @Override
                                        public void done(AVIMClient avimClient, AVIMException e) {
                                        }
                                    });
                                    PushManager.getInstance().unsubscribeCurrentUserChannel();
                                    AVUser.logOut();
                                    PushManager.getInstance().unsubscribeCurrentUserChannel();
                                    LeanchatUser.logOut();
                                    startActivity(new Intent(AuditActivity.this, LoginActivity.class));
                                    finish();

                                }
                            }
                        });

                    } else if ("0".equals(stepInfo.getStatus())) {
                        Toast.makeText(AuditActivity.this, stepInfo.getData(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case 404:
                    Log.e("AuditActivity", "-失败了-");
                    if (pdialog != null) {
                        pdialog.dismiss();
                    }
                    Utils.toast("网络异常,请稍后再试");

                    break;
            }
        }
    };
    private String userphone;
    private String jPushTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_audit);
        Intent intent = getIntent();
        userphone = intent.getStringExtra("phone");
        //   userphone = "15719212113";
        initviews();
        initlisteners();
        inittitle();
        initdatas();
    }

    private void inittitle() {
        btn_tijao.setVisibility(View.INVISIBLE);
        tv_top.setText("工程师审核");
    }

    private void initviews() {
        back = (ImageView) findViewById(R.id.back);
        btn_tijao = (Button) findViewById(R.id.btn_tijao);
        tv_top = (TextView) findViewById(R.id.tv_top);
        et_name = (EditText) findViewById(R.id.et_name);
        et_namenum1 = (EditText) findViewById(R.id.et_namenum);
        et_password = (EditText) findViewById(R.id.et_password);
        et_twoPssword = (EditText) findViewById(R.id.et_twoPssword);
        gridView1 = (GridView) findViewById(R.id.gridView1);
        et_submit = (Button) findViewById(R.id.et_submit);
    }

    private void initlisteners() {
        back.setOnClickListener(this);
        et_submit.setOnClickListener(this);
    }


    private void initdatas() {
        showHintDialog();
        showAddPhoto();
    }

    // 特别提示dialog
    private void showHintDialog() {
        final TvDialog tvDialog = new TvDialog(AuditActivity.this, R.style.mytvdialog);
        tvDialog.show();
        tvDialog.setOnDialogClickListen(new DialogListener() {
            @Override
            public void click() {
                if (tvDialog != null) {
                    tvDialog.dismiss();
                }

            }
        });

    }

    //处理gridView1  添加图片等事件
    private void showAddPhoto() {
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.addicon);
        imageItem = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemImage", bmp);
        imageItem.add(map);

        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);
        gridView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    dialog(position);
                }
                return false;
            }
        });
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 7) { //第一张为默认图片
                    Toast.makeText(AuditActivity.this, "图片数6张已满", Toast.LENGTH_SHORT).show();
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    Toast.makeText(AuditActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    //选择图片
                   /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image*//*");
                    startActivityForResult(intent, IMAGE_OPEN);*/
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, IMAGE_OPEN);
                    //通过onResume()刷新数据
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.et_submit:

                putData();
                break;
        }
    }

    // 提交数据
    private void putData() {
        String name = et_name.getText().toString();
        String namenum = et_namenum1.getText().toString();
        String password = et_password.getText().toString();
        String twoPssword = et_twoPssword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AuditActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(namenum)) {
            Toast.makeText(AuditActivity.this, "请输入身份证号", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(AuditActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(AuditActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(twoPssword)) {
            Toast.makeText(AuditActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(twoPssword)) {
            Toast.makeText(AuditActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("AuditActivity", "0004---" + listitem.size());
        if (listitem.size() == 0) {
            Toast.makeText(AuditActivity.this, "请上传专业证书照片", Toast.LENGTH_LONG).show();
        } else {
            AuditBean uploadPhotoInfo = new AuditBean();
            for (int i = 0; i < listitem.size(); i++) {
                pdialog = ProgressDialog.show(AuditActivity.this, "正在加载...", "系统正在处理您的请求");
                String url = Constants.URL + "cloundEngineer/engineerRegister";
                Log.e("AuditActivity", "0004---" + listitem.get(i).getPath());
                uploadPhotoInfo.setImage(listitem.get(i).getPath());
                if (userphone != null) {
                    uploadPhotoInfo.setTelephone(userphone);
                }
                uploadPhotoInfo.setUserName(name);
                uploadPhotoInfo.setPassword(password);
                uploadPhotoInfo.setUserID(namenum);
                MyHttpUtils.handData(handler, 39, url, uploadPhotoInfo);
            }
        }
    }

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
        JPushInterface.onResume(this);
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AuditActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                listitem.remove(position - 1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            try {
                Uri photoUri = data.getData();
                String[] pojo = {MediaStore.MediaColumns.DATA};
                cursor = AuditActivity.this.getContentResolver().query(photoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    pathImage = cursor.getString(columnIndex);
                    Log.e("AuditActivity", "0006----" + pathImage);
                    ImageItem imageItem = new ImageItem();
                    imageItem.setPath(pathImage);
                    listitem.add(imageItem);
                    if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                        cursor.close();
                    }
                    Log.e("AuditActivity", "0005----" + imageItem.getPath());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();// 用户点击取消操作
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (pdialog != null) {
            pdialog.dismiss();
        }
        return false;
    }

    public void setjPushTag(String jPushTag) {
        int action = -1;
        if (jPushTag == null) {
            return;
        }
        action = ACTION_SET;
        setjPushAction(jPushTag, action);
    }

    private void setjPushAction(String tags, int action) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        tagAliasBean.tags = getInPutTags(tags);
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
    }

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private Set<String> getInPutTags(String tags) {
        String tag = tags;
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                return null;
            }
            tagSet.add(sTagItme);
        }
        if (tagSet.isEmpty()) {
            return null;
        }
        return tagSet;
    }
}
