package kzy.com.gyyengineer.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import kzy.com.gyyengineer.leanchat.util.Utils;


/**
 */
public class MyCallBack extends RequestCallBack<String> {
    private Object object;
    private Handler handler;
    private int what;

    public MyCallBack(Object object, Handler handler, int what) {
        this.object = object;
        this.handler = handler;
        this.what = what;
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        Log.e("AuditActivity", "--responseInfo.result--" + responseInfo.result);
        object = new Gson().fromJson(responseInfo.result, object.getClass());
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = object;
        handler.sendMessage(msg);

    }

    @Override
    public void onFailure(HttpException error, String msg) {
        Log.e("AuditActivity", "--error--" + error+"---msg---"+msg);

        Message msgErr = Message.obtain();
        msgErr.what = 404;
        handler.sendMessage(msgErr);
    }
}
