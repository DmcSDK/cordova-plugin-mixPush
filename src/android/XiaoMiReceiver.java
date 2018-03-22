package com.dmc.push;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by dmcBig on 2017/5/24.
 */

public class XiaoMiReceiver  extends PushMessageReceiver {


    /**
     * onReceivePassThroughMessage用来接收服务器发送的透传消息
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        Log.e("TAG", "onReceivePassThroughMessage");
        try {
            ShortcutBadger.applyCount(context,Integer.parseInt(miPushMessage.getExtra().get("unrecv")));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        MixPushPlugin.onNotificationMessageArrivedCallBack(msgToJson(miPushMessage), miPushMessage.getExtra());        
    }

    /**
     * onNotificationMessageClicked用来接收服务器发来的通知栏消息（用户点击通知栏时触发）
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        Log.e("TAG", "onNotificationMessageClicked");
        if(!isRunningForeground(context)) {//后台时候提
            Intent launch = context.getPackageManager().getLaunchIntentForPackage(
                    context.getPackageName());
            launch.addCategory(Intent.CATEGORY_LAUNCHER);
            launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(launch);
        }
        MixPushPlugin.onNotificationMessageClickedCallBack(msgToJson(miPushMessage), miPushMessage.getExtra());
    }

    /**
     * onNotificationMessageArrived用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        Log.e("TAG", "onNotificationMessageArrived");
        try {
            ShortcutBadger.applyCount(context,Integer.parseInt(miPushMessage.getExtra().get("unrecv")));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        MixPushPlugin.onNotificationMessageArrivedCallBack(msgToJson(miPushMessage), miPushMessage.getExtra());
    }

    public  JSONObject msgToJson( MiPushMessage miPushMessage){
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("content",miPushMessage.getContent());
            jsonObject.put("messageId",miPushMessage.getMessageId());
            jsonObject.put("messageType",miPushMessage.getMessageType());
            jsonObject.put("alias",miPushMessage.getAlias());
            jsonObject.put("topic",miPushMessage.getTopic());
            jsonObject.put("user_account",miPushMessage.getUserAccount());
            jsonObject.put("passThrough",miPushMessage.getPassThrough());
            jsonObject.put("notifyType",miPushMessage.getNotifyType());
            jsonObject.put("notifyId",miPushMessage.getNotifyId());
            jsonObject.put("description",miPushMessage.getDescription());
            jsonObject.put("title",miPushMessage.getTitle());
            jsonObject.put("category",miPushMessage.getCategory());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * onCommandResult用来接收客户端向服务器发送命令消息后返回的响应
     *
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.e("TAG", "onCommandResult");
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {  //registerPush

            sendPluginResult("regId","","onRegisterPush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {

            sendPluginResult("alias","","onSetAliasPush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {

            sendPluginResult("alias","","onUnSetAliasPush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {

            sendPluginResult("account","","onSetAccountPush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {

            sendPluginResult("account","","onUnSetAccountPush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {

            sendPluginResult("topic","","onSubscribePush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {

            sendPluginResult("topic","","onUnSubscribePush",message.getResultCode(),cmdArg1,cmdArg2);

        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {

            sendPluginResult("startTime","endTime","onSetAcceptTimePush",message.getResultCode(),cmdArg1,cmdArg2);

        }
    }

    void sendPluginResult(String commandStr1,String commandStr2,String event,long messageCode,String cmdArg1,String cmdArg2 ){
        try {
            JSONObject jsonObject = new JSONObject();
            if(event.equals("onSetAcceptTimePush")){
                jsonObject.put(commandStr1,cmdArg1);
                jsonObject.put(commandStr2,cmdArg2);
            }else{
                jsonObject.put(commandStr1,cmdArg1);
            }
            if (messageCode == ErrorCode.SUCCESS) {
                MixPushPlugin.onCommandResult(event, 200, jsonObject);
            } else {
                MixPushPlugin.onCommandResult(event, 500, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取显示在最顶端的activity名称
     * @param context
     * @return
     */
    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =(ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }
    /**
     * 获取当前应用包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 判断是否运行在前台
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);

        if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {

            return true;
        } else {

            return false;
        }
    }
}
