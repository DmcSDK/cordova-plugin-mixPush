package com.dmc.push;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushClient;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by dmcBig on 2017/5/22.
 */

public class XiaoMiImpl implements IPush {

    MixPushPlugin mixPushPlugin;
    private static String TAG = "XiaoMiImpl";

    public XiaoMiImpl(MixPushPlugin mixPushPlugin){
        this.mixPushPlugin=mixPushPlugin;
    }

    @Override
    public void registerPush(CallbackContext callback, Context context, JSONArray args) {
        try {
            if (shouldInit(context)) {
                MiPushClient.registerPush(context, args.getString(0), args.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("registerPush");
        }
    }

    @Override
    public void exitPush(CallbackContext callback, Context context, JSONArray args) {
        Log.e(TAG, "---------exitPush-----------");
        try {
            MiPushClient.unregisterPush(context);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("exitPush:error--" + e.toString());
        }
    }

    @Override
    public String getRegId(CallbackContext callback, Context context, JSONArray args) {
        Log.e(TAG, "---------getRegId-----------");
        String id="";
        try {
            id=MiPushClient.getRegId(context);
            callback.success(id);
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("getRegId:error--" + e.toString());
        }
        return id;
    }

    @Override
    public void setAccount(CallbackContext callback, Context context, String account, JSONArray other) {
        Log.e(TAG, "---------setAccount-----------");
        try {
            Log.e(TAG, "-----------setAccount-------------" + account);
            MiPushClient.setUserAccount(context, account, null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("setAccount:error--" + e.toString());
        }
    }

    @Override
    public void unsetAccount(CallbackContext callback, Context context, String account, JSONArray other) {
        Log.e(TAG, "---------unsetAccount-----------");
        try {
            Log.e(TAG, "-----------unsetAccount-------------" + account);
            MiPushClient.unsetUserAccount(context, account, null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("setAccount:error--" + e.toString());
        }
    }

    @Override
    public void setAlias(CallbackContext callback, Context context, String alias, JSONArray other) {
        Log.e(TAG, "---------setAlias-----------");
        try {
            Log.e(TAG, "-----------alias-------------" + alias);
            MiPushClient.setAlias(context, alias, null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("setAlias:error--" + e.toString());
        }
    }

    @Override
    public void unsetAlias(CallbackContext callback, Context context, String alias, JSONArray other) {
        Log.e(TAG, "---------unsetAlias-----------");
        try {
            Log.e(TAG, "-----------alias-------------" + alias);
            MiPushClient.unsetAlias(context, alias, null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("unsetAlias:error--" + e.toString());
        }
    }

    @Override
    public void subscribe(CallbackContext callback, Context context, String topic, JSONArray other) {
        Log.e(TAG, "---------subscribe-----------");
        try {
            Log.e(TAG, "-----------subscribe-------------" + topic);
            MiPushClient.subscribe(context, topic, null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("subscribe:error--" + e.toString());
        }
    }

    @Override
    public void unsubscribe(CallbackContext callback, Context context, String topic, JSONArray other) {
        Log.e(TAG, "---------unsubscribe-----------");
        try {
            Log.e(TAG, "-----------unsubscribe-------------" + topic);
            MiPushClient.unsubscribe(context, topic, null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("unsubscribe:error--" + e.toString());
        }
    }

    @Override
    public void pausePush(CallbackContext callback, Context context, String category, JSONArray other) {
        Log.e(TAG, "---------pausePush-----------");
        try {
            MiPushClient.pausePush(context,null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("pausePush:error--" + e.toString());
        }
    }

    @Override
    public void resumePush(CallbackContext callback, Context context, String category, JSONArray other) {
        Log.e(TAG, "---------resumePush-----------");
        try {
            MiPushClient.resumePush(context,null);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("resumePush:error--" + e.toString());
        }
    }

    @Override
    public void disablePush(CallbackContext callback, Context context, JSONArray other) {
        Log.e(TAG, "---------disablePush-----------");
        try {
            MiPushClient.disablePush(context);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("disablePush:error--" + e.toString());
        }
    }

    @Override
    public void enablePush(CallbackContext callback, Context context, JSONArray other) {
        Log.e(TAG, "---------enablePush-----------");
        try {
            MiPushClient.enablePush(context);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("enablePush:error--" + e.toString());
        }
    }

    @Override
    public void clearNotification(CallbackContext callback, Context context, JSONArray args) {
        Log.e(TAG, "---------clearNotification-----------");
        try {
            MiPushClient.clearNotification(context);
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("clearNotification:error--" + e.toString());
        }
    }

    @Override
    public void clearNotificationById(CallbackContext callback, Context context, JSONArray args) {
        Log.e(TAG, "---------clearNotificationById-----------");
        try {
            MiPushClient.clearNotification(context,args.getInt(0));
            callback.success();
        } catch (Exception e) {
            e.printStackTrace();
            callback.error("clearNotificationById:error--" + e.toString());
        }
    }


    //通过判断手机里的所有进程是否有这个App的进程,从而判断该App是否有打开
    private boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
