package kzy.com.gyyengineer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yunzhanghu.redpacketsdk.RPInitRedPacketCallback;
import com.yunzhanghu.redpacketsdk.RPValueCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.bean.TokenData;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.jpush.android.api.JPushInterface;
import cn.leancloud.chatkit.LCChatKit;
import kzy.com.gyyengineer.constant.Constants;
import kzy.com.gyyengineer.constant.ImgConfig;
import kzy.com.gyyengineer.leanchat.controller.ChatManager;
import kzy.com.gyyengineer.leanchat.friends.AddRequest;
import kzy.com.gyyengineer.leanchat.model.LCIMRedPacketAckMessage;
import kzy.com.gyyengineer.leanchat.model.LCIMRedPacketMessage;
import kzy.com.gyyengineer.leanchat.model.LCIMTransferMessage;
import kzy.com.gyyengineer.leanchat.model.LeanchatUser;
import kzy.com.gyyengineer.leanchat.model.UpdateInfo;
import kzy.com.gyyengineer.leanchat.redpacket.GetSignInfoCallback;
import kzy.com.gyyengineer.leanchat.redpacket.RedPacketUtils;
import kzy.com.gyyengineer.leanchat.service.ConversationManager;
import kzy.com.gyyengineer.leanchat.service.PushManager;
import kzy.com.gyyengineer.leanchat.util.LeanchatUserProvider;
import kzy.com.gyyengineer.leanchat.util.Utils;
import kzy.com.gyyengineer.utils.CustomConstants;

public class App extends MultiDexApplication implements
        UncaughtExceptionHandler {
    public static App ctx;
    public static SharedPreferences sharedPreferences;
    public static boolean debug = true;

    public static App getInstance() {
        return ctx;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        initjPush();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        ImgConfig.initImageLoader();
        Utils.fixAsyncTaskBug();

        String appId = "vKRw1RlusfkSU4YpPNYyOMN9-gzGzoHsz";
        String appKey = "Bx93CsiFIuQUT396O1GJpMAa";
        LeanchatUser.alwaysUseSubUserClass(LeanchatUser.class);
        AVObject.registerSubclass(AddRequest.class);
        AVObject.registerSubclass(UpdateInfo.class);
// 初始化参数依次为 this, AppId, AppKey


        //       AVOSCloud.initialize(this,"9NysbzQoP8bsfnabQbslHMPU-gzGzoHsz","t7wPL1BbnCoQvrwcvDbQAUYS");

        AVIMMessageManager.registerAVIMMessageType(LCIMRedPacketMessage.class);
        AVIMMessageManager.registerAVIMMessageType(LCIMRedPacketAckMessage.class);
        AVIMMessageManager.registerAVIMMessageType(LCIMTransferMessage.class);
        LCChatKit.getInstance().setProfileProvider(new LeanchatUserProvider());
        LCChatKit.getInstance().init(this, appId, appKey);
        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);

        PushManager.getInstance().init(ctx);
        AVOSCloud.setDebugLogEnabled(debug);
//        AVAnalytics.enableCrashReport(this, !debug);
        initImageLoader(ctx);
        initChatManager();

        if (App.debug) {
            openStrictMode();
        }
        removeTempFromPref();
        // 初始化红包操作
        RedPacket.getInstance().initRedPacket(ctx, RPConstant.AUTH_METHOD_SIGN, new RPInitRedPacketCallback() {
            @Override
            public void initTokenData(final RPValueCallback<TokenData> rpValueCallback) {
                RedPacketUtils.getInstance().getRedPacketSign(ctx, new GetSignInfoCallback() {
                    @Override
                    public void signInfoSuccess(TokenData tokenData) {
                        rpValueCallback.onSuccess(tokenData);
                    }

                    @Override
                    public void signInfoError(String errorMsg) {
                    }
                });
            }

            @Override
            public RedPacketInfo initCurrentUserSync() {
                //这里需要同步设置当前用户id、昵称和头像url
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.fromUserId = LeanchatUser.getCurrentUserId();
                redPacketInfo.fromAvatarUrl = LeanchatUser.getCurrentUser().getAvatarUrl();
                redPacketInfo.fromNickName = LeanchatUser.getCurrentUser().getUsername();
                return redPacketInfo;
            }
        });
        //控制红包SDK中Log输出
        RedPacket.getInstance().setDebugMode(false);

        SDKInitializer.initialize(getApplicationContext());
    }

    private void initjPush() {
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }


    private void initChatManager() {
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        if (LeanchatUser.getCurrentUser() != null) {
            chatManager.setupManagerWithUserId(LeanchatUser.getCurrentUser()
                    .getObjectId());
        }
        chatManager.setConversationEventHandler(ConversationManager
                .getEventHandler());
        ChatManager.setDebugEnabled(App.debug);
    }

    public void openStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                //.penaltyDeath()
                .build());
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    private void removeTempFromPref() {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).commit();
    }

}
