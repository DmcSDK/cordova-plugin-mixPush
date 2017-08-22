package com.dmc.push;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

/**
 * Created by dmcBig on 2017/5/22.
 */

public class HuaWeiImpl implements IPush {

    MixPushPlugin mixPushPlugin;

    public HuaWeiImpl(MixPushPlugin mixPushPlugin){
        this.mixPushPlugin=mixPushPlugin;
    }

    @Override
    public void registerPush(CallbackContext callback, Context context, JSONArray args) {

    }

    @Override
    public void exitPush(CallbackContext callback, Context context, JSONArray args) {

    }

    @Override
    public String getRegId(CallbackContext callback, Context context, JSONArray args) {
        return null;
    }

    @Override
    public void setAccount(CallbackContext callback, Context context, String account, JSONArray other) {

    }

    @Override
    public void unsetAccount(CallbackContext callback, Context context, String account, JSONArray other) {

    }

    @Override
    public void setAlias(CallbackContext callback, Context context, String alias, JSONArray other) {

    }

    @Override
    public void unsetAlias(CallbackContext callback, Context context, String alias, JSONArray other) {

    }

    @Override
    public void subscribe(CallbackContext callback, Context context, String topic, JSONArray other) {

    }

    @Override
    public void unsubscribe(CallbackContext callback, Context context, String topic, JSONArray other) {

    }

    @Override
    public void pausePush(CallbackContext callback, Context context, String category, JSONArray other) {

    }

    @Override
    public void resumePush(CallbackContext callback, Context context, String category, JSONArray other) {

    }

    @Override
    public void disablePush(CallbackContext callback, Context context, JSONArray other) {

    }

    @Override
    public void enablePush(CallbackContext callback, Context context, JSONArray other) {

    }

    @Override
    public void clearNotification(CallbackContext callback, Context context, JSONArray args) {

    }

    @Override
    public void clearNotificationById(CallbackContext callback, Context context, JSONArray args) {

    }
}
