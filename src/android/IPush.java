package com.dmc.push;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

/**
 * Created by lenovo on 2017/5/19.
 */

public interface IPush {
    /**
     * 启动和注册推送
     */
     void registerPush(CallbackContext callback,Context context, JSONArray args);

    /**
     * 退出并清空账号ID及相关设置
     */
     void exitPush(CallbackContext callback,Context context, JSONArray args);

    /**
     * 获取注册第三方推送后的唯一标识
     * @return
     */
     String getRegId(CallbackContext callback,Context context, JSONArray args);

     void setAccount(CallbackContext callback,Context context,String account, JSONArray other);

     void unsetAccount(CallbackContext callback,Context context,String account, JSONArray other);

     void setAlias(CallbackContext callback,Context context,String alias, JSONArray other);

     void unsetAlias(CallbackContext callback,Context context,String alias, JSONArray other);

     void subscribe(CallbackContext callback,Context context,String topic, JSONArray other);

     void unsubscribe(CallbackContext callback,Context context,String topic, JSONArray other);

    /**
     * 暂停推送，当下次调用resumePush时恢复推送，！注意：下次恢复推送会把暂停期间的消息重新推送过来。
     * @param context
     * @param category
     * @param other
     */
     void pausePush(CallbackContext callback,Context context,String category, JSONArray other);
    /**
     * 恢复推送，注意：下次恢复推送会把暂停期间的消息重新推送过来。
     * @param context
     * @param category
     * @param other
     */
     void resumePush(CallbackContext callback,Context context,String category, JSONArray other);

    /**
     * 停止推送，下次enablePush也不会收到停止期间的消息
      * @param context
     * @param other
     */
     void disablePush(CallbackContext callback,Context context,JSONArray other);

    /**
     * 恢复推送，不会收到disablePush期间的消息
     * @param context
     * @param other
     */
     void enablePush(CallbackContext callback,Context context,JSONArray other);

     void clearNotification(CallbackContext callback,Context context,JSONArray args);
     void clearNotificationById(CallbackContext callback,Context context,JSONArray args);
}

