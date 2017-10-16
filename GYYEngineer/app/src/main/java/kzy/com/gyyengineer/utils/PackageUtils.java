package kzy.com.gyyengineer.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 创建日期：2017/6/22 0022 on 14:28
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class PackageUtils {
    public static String getPackageVersion(Context context){
        String version = "1.0";
        //获取PackageManager
        PackageManager packageManager = context.getPackageManager();

        try {
            //获取PackageInfo
            PackageInfo packageInfo =
                    packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            //获取版本号
            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  version;
    }
}
