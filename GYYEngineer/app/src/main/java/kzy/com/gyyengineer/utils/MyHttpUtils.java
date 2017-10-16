package kzy.com.gyyengineer.utils;

import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

import kzy.com.gyyengineer.model.AuditBean;
import kzy.com.gyyengineer.model.ForGetpass;
import kzy.com.gyyengineer.model.Info;
import kzy.com.gyyengineer.model.OtherGoodSayBean;
import kzy.com.gyyengineer.model.Result;
import kzy.com.gyyengineer.model.StepInfo;
import kzy.com.gyyengineer.model.TiUser;
import kzy.com.gyyengineer.model.UserInfo;
import kzy.com.gyyengineer.model.UserReg;


/**
 * 创建日期：2017/6/15 0016 on 13:31
 * 描述：网络请求封装类
 * 作者：赵金祥  Administrator
 */
public class MyHttpUtils extends HttpUtils {
    private static MyHttpUtils httpUtils = new MyHttpUtils();

    public static void sendData(HttpRequest.HttpMethod method, String url, RequestParams params, RequestCallBack requestCallBack) {
        if (method == HttpRequest.HttpMethod.GET) {
            httpUtils.send(method, url, requestCallBack);
        } else if (method == HttpRequest.HttpMethod.POST) {
            httpUtils.send(method, url, params, requestCallBack);
        }
    }

    public static void handData(Handler handler, int what, String url, Object object) {
        httpUtils.configCurrentHttpCacheExpiry(6000);// 设置超时时间
        httpUtils.configTimeout(6000);// 连接超时  //指的是连接一个url的连接等待时间。
        httpUtils.configSoTimeout(6000);// 获取数据超时  //指的是连接上一个url，获取response的返回等待时间
        RequestParams params = new RequestParams();
        switch (what) {
            case 27://修改密码
                params.addBodyParameter("telephone", ((UserInfo) object).getPhone());
                params.addBodyParameter("password", ((UserInfo) object).getPass());
                params.addBodyParameter("oldPassword", ((UserInfo) object).getOldpwd());
                Log.e("ChangePwdActivity", "--修改密码参数--" + ((UserInfo) object).getPhone() + "--" + ((UserInfo) object).getPass() + "---" + ((UserInfo) object).getOldpwd());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new Result(), handler, what));
                break;
            case 11://用户注册
                UserInfo userInfo = (UserInfo) object;
                params.addBodyParameter("telephone", userInfo.getPhone());
                params.addBodyParameter("password", userInfo.getPass());
                params.addBodyParameter("userName", userInfo.getName());
                params.addBodyParameter("engineerCode", userInfo.getQiNumber());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new UserReg(), handler, what));
                break;
            case 39://非公司工程师审核
                AuditBean auditBean = (AuditBean) object;
                params.addBodyParameter("telephone", auditBean.getTelephone());
                params.addBodyParameter("password", auditBean.getPassword());
                params.addBodyParameter("userName", auditBean.getUserName());
                params.addBodyParameter("userID", auditBean.getUserID());
                params.addBodyParameter("image", new File(auditBean.getImage()));
                Log.e("AuditActivity", "0004---" + auditBean.getTelephone() + "---" + auditBean.getPassword() + "---" + auditBean.getUserName() + "---" + auditBean.getUserID());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new UserReg(), handler, what));
                break;
            case 12://用户登录
                UserInfo userInf = (UserInfo) object;
                params.addBodyParameter("telephone", userInf.getPhone());
                params.addBodyParameter("password", userInf.getPass());
                params.addBodyParameter("engineerId", userInf.getObjectId());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new UserReg(), handler, what));
                break;
            case 13://上传认证头像
                UserInfo user = (UserInfo) object;
                params.addBodyParameter("phone", user.getPhone());
                params.addBodyParameter("imagePath", new File(user.getPass()));
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new Info(), handler, what));
                break;
            case 14://上传认证头像
                UserInfo ser = (UserInfo) object;
                params.addBodyParameter("phone", ser.getPhone());
                params.addBodyParameter("imagePath", new File(ser.getPass()));
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new Info(), handler, what));
                break;
            case 16://
                params.addBodyParameter("telephone", ((UserInfo) object).getPhone());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new ForGetpass(), handler, what));
                break;
            case 17://忘记密码
                params.addBodyParameter("telephone", ((UserInfo) object).getPhone());
                params.addBodyParameter("password", ((UserInfo) object).getPass());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new Result(), handler, what));
                break;

            case 29://第一次注册
                UserInfo user2Info = (UserInfo) object;
                params.addBodyParameter("telephone", user2Info.getPhone());

                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new UserReg(), handler, what));
                break;

            case 38://上传头像
                TiUser tiUser = (TiUser) object;
                params.addBodyParameter("telephone", tiUser.getName() + "");
                String tel = tiUser.getTel();
                String name = tiUser.getName();
                params.addBodyParameter("imgUrl", tiUser.getTel() + "");
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new StepInfo(), handler, what));
                break;

            case 266://向服务端传用户图像
                params.addBodyParameter("telephone", ((TiUser) object).getCardId());
                params.addBodyParameter("imgUrl", ((TiUser) object).getTel());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new StepInfo(), handler, what));
                break;
            case 267://其他好友好评率信息
                params.addBodyParameter("telephone", ((TiUser) object).getCardId());
                sendData(HttpRequest.HttpMethod.POST, url, params, new MyCallBack(new OtherGoodSayBean(), handler, what));
                break;
        }
    }
}
