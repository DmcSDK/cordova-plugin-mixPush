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

import java.util.HashMap;
import java.util.Map;

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

    enum ActionType {
        SETPUSHENGINE("setPushEngine"),
        REGISTERPUSH("registerPush"),
        EXITPUSH("exitPush"),
        SETACCOUNT("setAccount"),
        UNSETACCOUNT("unsetAccount"),
        GETREGID("getRegId"),
        SETALIAS("setAlias"),
        UNSETALIAS("unsetAlias"),
        SUBSCRIBE("subscribe"),
        UNSUBSCRIBE("unsubscribe"),
        PAUSEPUSH("pausePush"),
        RESUMEPUSH("resumePush"),
        DISABLEPUSH("disablePush"),
        ENABLEPUSH("enablePush"),
        CLEARNOTIFICATION("clearNotification"),
        CLEARNOTIFICATIONBYID("clearNotificationById"),
        BADGERAPPLYCOUNT("badgerApplyCount"),
        BADGERREMOVECOUNT("badgerRemoveCount"),
        BADGERMINUSCOUNT("badgerMinusCount");
        ActionType(String value) {
            this.name = value;
        }

        private String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private static Map<String, ActionType> map;

        static {
            map = new HashMap<String, ActionType>();
            for (ActionType actionType : ActionType.values()) {
                map.put(actionType.getName(), actionType);
            }
        }

        public static ActionType findByValue(String value) {
            return map.get(value);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        ActionType type = ActionType.findByValue(action);
        if (type == null) {
            Log.w(TAG, "invalid param, action: " + action);
            return false;
        }

        switch (type) {
            case SETPUSHENGINE:
                setPushEngine(args.getString(0));
                return true;
            case REGISTERPUSH:
                pushEngine.registerPush(callbackContext, cordova.getActivity(), args);
                return true;
            case EXITPUSH:
                pushEngine.exitPush(callbackContext, cordova.getActivity(), args);
                return true;
            case SETACCOUNT:
                pushEngine.setAccount(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case UNSETACCOUNT:
                pushEngine.unsetAccount(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case GETREGID:
                pushEngine.getRegId(callbackContext, cordova.getActivity(), args);
                return true;
            case SETALIAS:
                pushEngine.setAlias(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case UNSETALIAS:
                pushEngine.unsetAlias(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case SUBSCRIBE:
                pushEngine.subscribe(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case UNSUBSCRIBE:
                pushEngine.unsubscribe(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case PAUSEPUSH:
                pushEngine.pausePush(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case RESUMEPUSH:
                pushEngine.resumePush(callbackContext, cordova.getActivity(), args.getString(0), args);
                return true;
            case DISABLEPUSH:
                pushEngine.disablePush(callbackContext, cordova.getActivity(), args);
                return true;
            case ENABLEPUSH:
                pushEngine.enablePush(callbackContext, cordova.getActivity(), args);
                return true;
            case CLEARNOTIFICATION:
                pushEngine.clearNotification(callbackContext, cordova.getActivity(), args);
                return true;
            case CLEARNOTIFICATIONBYID:
                pushEngine.clearNotificationById(callbackContext, cordova.getActivity(), args);
                return true;
            case BADGERAPPLYCOUNT:
                try {
                    ShortcutBadger.applyCount(cordova.getActivity(), args.getInt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case BADGERREMOVECOUNT:
                try {
                    ShortcutBadger.removeCount(cordova.getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case BADGERMINUSCOUNT:
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
