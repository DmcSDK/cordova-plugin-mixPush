package com.dmc.push;

import android.content.SharedPreferences;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dmcBig on 2017/5/18.
 */

public class MixPushPlugin extends CordovaPlugin {
    private static String TAG = "MiPushPlugin";
    private static MixPushPlugin instance;
    IPush pushEngine;
    public MixPushPlugin() {
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    public void setPushEngine(String push){
        if("xiaoMi".equals(push)){
            pushEngine=new XiaoMiImpl(this);
        }else if("huaWei".equals(push)){
            pushEngine=new HuaWeiImpl(this);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action){
            case "setPushEngine":
                setPushEngine(args.getString(0));
                return true;
            case "registerPush":
                pushEngine.registerPush(callbackContext,cordova.getActivity(),args);
                return true;
            case "exitPush":
                pushEngine.exitPush(callbackContext,cordova.getActivity(),args);
                return true;
            case "setAccount":
                pushEngine.setAccount(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "unsetAccount":
                pushEngine.unsetAccount(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "getRegId":
                pushEngine.getRegId(callbackContext,cordova.getActivity(),args);
                return true;
            case "setAlias":
                pushEngine.setAlias(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "unsetAlias":
                pushEngine.unsetAlias(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "subscribe":
                pushEngine.subscribe(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "unsubscribe":
                pushEngine.unsubscribe(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "pausePush":
                pushEngine.pausePush(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "resumePush":
                pushEngine.resumePush(callbackContext,cordova.getActivity(),args.getString(0),args);
                return true;
            case "disablePush":
                pushEngine.disablePush(callbackContext,cordova.getActivity(),args);
                return true;
            case "enablePush":
                pushEngine.enablePush(callbackContext,cordova.getActivity(),args);
                return true;
            case "clearNotification":
                pushEngine.clearNotification(callbackContext,cordova.getActivity(),args);
                return true;
            case "clearNotificationById":
                pushEngine.clearNotificationById(callbackContext,cordova.getActivity(),args);
                return true;
            case "badgerApplyCount":
                try {
                    ShortcutBadger.applyCount(cordova.getActivity(), args.getInt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case "badgerRemoveCount":
                try {
                    ShortcutBadger.removeCount(cordova.getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case "badgerMinusCount":
                try {
                    minusBadgerToSp(args.getInt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
                default:
                    break;
        }
        return false;
    }


    public static void  addBadgerToSp(){
        SharedPreferences sp = instance.cordova.getActivity().getSharedPreferences("Badger", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("count", sp.getInt("count",0)+1);
        editor.commit();
        Log.e(TAG,sp.getInt("count",0)+"");
        ShortcutBadger.applyCount(instance.cordova.getActivity(),sp.getInt("count",0));
    }


    public static void minusBadgerToSp(int n){
        SharedPreferences sp = instance.cordova.getActivity().getSharedPreferences("Badger", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("count", sp.getInt("count",0)-n);
        editor.commit();
        ShortcutBadger.applyCount(instance.cordova.getActivity(),sp.getInt("count",0));
    }

    /**
     * 接受到消息
     */
    public static void onNotificationMessageArrivedCallBack(JSONObject jsonObject,Object other) {
        Log.e(TAG, "-------------onNotificationArrived------------------");
        if (instance == null) {
            return;
        }
        Log.e(TAG, "-------------onNotificationArrived------------------" + jsonObject.toString());
        String format = "window.plugins.MixPushPlugin.onNotificationArrived(%s);";
        final String js = String.format(format, jsonObject.toString());
        instance.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }


    public static void onNotificationMessageClickedCallBack(JSONObject jsonObject,Object other) {
        Log.e(TAG, "-------------onNotificationClicked------------------");
        if (instance == null) {
            return;
        }
        Log.e(TAG, "-------------onNotificationClicked------------------" + jsonObject.toString());
        String format = "window.plugins.MixPushPlugin.onNotificationClicked(%s);";
        final String js = String.format(format, jsonObject.toString());
        instance.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }



    public static void onCommandResult(String type,int code,JSONObject jsonObject) {
        Log.e(TAG, "-------------onCommandResult------------------" + code);
        if (instance == null) {
            return;
        }
        try {
            jsonObject.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String format = "window.plugins.MixPushPlugin."+type+"(%s);";
        final String js = String.format(format,jsonObject.toString());
        instance.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });

    }
}
