package kzy.com.gyyengineer.leanchat.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.engineer.activity.AboutActivity;
import kzy.com.gyyengineer.engineer.activity.GoodSeyActivity;
import kzy.com.gyyengineer.engineer.activity.LoginActivity;
import kzy.com.gyyengineer.engineer.activity.MipcaActivityCapture;
import kzy.com.gyyengineer.engineer.activity.SettingActivity;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.util.PathUtils;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.model.StepInfo;
import kzy.com.gyyengineer.model.TiUser;
import kzy.com.gyyengineer.utils.MyAndroidUtil;
import kzy.com.gyyengineer.utils.MyHttpUtils;
import kzy.com.gyyengineer.utils.PhotoUtils;
import util.UpdateAppUtils;

/**
 * Created by lzw on 14-9-17.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final int RESULTCODE_LOGOUT_FINISH_OK = 88;
    private static final int MAKE_PWD_REQUEST_OK = 0;
    private static final int MAKE_PWD_RESULT_OK = 12;  // 修改密码成功 返回结果码
    private final static int SCANNIN_GREQUEST_CODE = 10;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "userhead.jpg";
    @Bind(R.id.profile_avatar_view)
    ImageView headView;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.rl_version)
    RelativeLayout rlVersion;

    private RelativeLayout rl_setting;
    private RelativeLayout rl_about;
    private RelativeLayout rl_xxlr;
    private TextView cameraPic, albumPic;
    private File tempFile;
    private AlertDialog avatarDialog;
    private Bitmap bitmap;
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private RelativeLayout rl_hpl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rl_about = (RelativeLayout) view.findViewById(R.id.rl_about);
        rl_xxlr = (RelativeLayout) view.findViewById(R.id.rl_xxlr);
        rl_hpl = (RelativeLayout) view.findViewById(R.id.rl_hpl);
        initlisteners();
        getVersionData();
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 266:
                    Log.e("ProfileFragment", "--头像上传成功--" + msg.obj);
                    StepInfo stepInfo = (StepInfo) msg.obj;
                    break;
                case 267:// 检测版本更新
                    String result = (String) msg.obj;
                    int nowVersionCode = getVersionCode();// 当前版本号
                    try {
                        JSONObject jo = new JSONObject(result);
                        int versionCode = jo.getInt("version");
                        if (nowVersionCode < versionCode) {
                            // 有新版本
                            tvVersion.setText("发现新版本");
                            tvVersion.setTextColor(getActivity().getResources().getColor(R.color.lcim_commom_read));
                        } else {
                            // 没有新版本
                            tvVersion.setText("已是最新版本");
                            tvVersion.setTextColor(getActivity().getResources().getColor(R.color.dark_grey));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 268:// 点击了版本更新
                    String result2 = (String) msg.obj;
                    int nowVersionCode2 = getVersionCode();// 当前版本号
                    try {
                        JSONObject jo = new JSONObject(result2);
                        int versionCode = jo.getInt("version");
                        String url = jo.getString("url");
                        String versionName = jo.getString("versionName");
                        if (nowVersionCode2 < versionCode) {
                            // 有新版本
                            UpdateAppUtils.from(getActivity())
                                    .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                                    .serverVersionCode(versionCode)
                                    .serverVersionName(versionName)
                                    .apkPath(url)
                                    .update();
                        } else {
                            // 没有新版本
                            Utils.toast("当前已是最新版本");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void initlisteners() {
        rl_setting.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_xxlr.setOnClickListener(this);
        rl_hpl.setOnClickListener(this);
        rlVersion.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headerLayout.showTitle(R.string.profile_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ProfileFragment", "----130-----" + LeanchatUser.getCurrentUser());
        if (LeanchatUser.getCurrentUser() == null) {
            return;
        }
        LeanchatUser.getCurrentUser().fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                LeanchatUser user = (LeanchatUser) avObject;
                if (user != null) {
                    String avatarUrl = user.getAvatarUrl();
                    String name = user.getUsername();
                    username.setText(name.substring(1, name.length()));
                    MyAndroidUtil.editXmlByString(
                            Constants.icon, avatarUrl);
                    ImageLoader.getInstance().displayImage(avatarUrl, headView,
                            PhotoUtils.avatarImageOption);
                }
            }
        });

        AVAnalytics.onFragmentStart("my-list-fragment");
    }


    @OnClick(R.id.profile_avatar_layout)
    public void onAvatarClick() {
        AvatarDialog();
    }


    public void AvatarDialog() {

        avatarDialog = new AlertDialog.Builder(getContext()).create();
        avatarDialog.setCanceledOnTouchOutside(true);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.my_headicon, null);
        avatarDialog.show();
        avatarDialog.setContentView(v);
        avatarDialog.getWindow().setGravity(Gravity.CENTER);
        albumPic = (TextView) v.findViewById(R.id.album_pic);
        cameraPic = (TextView) v.findViewById(R.id.camera_pic);
        // 相册
        albumPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarDialog.dismiss();
                showAvatarFromAlbum();
            }
        });
        // 相机
        cameraPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarDialog.dismiss();
                showAvatarFromCamera();
            }
        });
    }

    private void showAvatarFromAlbum() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    private void showAvatarFromCamera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /*
* 判断sdcard是否被挂载
*/
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAKE_PWD_REQUEST_OK && resultCode == MAKE_PWD_RESULT_OK) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        }
        if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
                if (bitmap != null) {
                    headView.setImageBitmap(bitmap);
                    bitmap = PhotoUtils.toRoundCorner(bitmap, 10);
                    String path = PathUtils.getAvatarCropPath();
                    PhotoUtils.saveBitmap(path, bitmap);
                    LeanchatUser.getCurrentUser().saveAvatar(path, new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                LeanchatUser curUser = LeanchatUser.getCurrentUser(LeanchatUser.class);
                                String avatarUrl = curUser.getAvatarUrl();
                                String urll = Constants.URL + "cloundEngineer/userImageUpload";
                                TiUser useri = new TiUser();
                                String username = LeanchatUser.getCurrentUser().getUsername();
                                String phone = username.substring(1, username.length());
                                useri.setCardId(phone);
                                Log.e("MeFragment", "avatarUrl--" + avatarUrl);
                                useri.setTel(avatarUrl);
                                MyHttpUtils.handData(handler, 266, urll, useri);
                            }
                        }
                    });
                }
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == MAKE_PWD_REQUEST_OK && resultCode == RESULTCODE_LOGOUT_FINISH_OK) {
            getActivity().finish();
        }
    }

    /*
        * 剪切图片
        */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting:
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                //       intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent1, MAKE_PWD_REQUEST_OK);
                break;
            case R.id.rl_about:
                Intent intent2 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_xxlr:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.rl_hpl:
                startActivity(new Intent(getActivity(), GoodSeyActivity.class));
                break;
            case R.id.rl_version:
                getNetVersion();
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getNetVersion() {
        String url = Constants.URL + "cloudRepair/versionCheckEngineer";
        MyHttpUtils.sendData(HttpRequest.HttpMethod.GET, url, null, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Log.e("ProfileFragment", "---------------" + result);
                Message message = new Message();
                message.what = 268;
                message.obj = result;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    public int getVersionCode() {
        try {
            int versionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public void getVersionData() {
        String url = Constants.URL + "cloudRepair/versionCheckEngineer";
        MyHttpUtils.sendData(HttpRequest.HttpMethod.GET, url, null, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                Message message = new Message();
                message.what = 267;
                message.obj = result;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
}
