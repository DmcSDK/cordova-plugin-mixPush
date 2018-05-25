# cordova-plugin-mixPush
Cordova 推送，支持设置角标，事件回调。
此项目代码设计以混合推送为基础，兼容多种推送引擎。

注意：此插件的依赖的组件来自mvn仓库，所以需要翻墙，JAVA SDK 需要1.7版本低于1.7会编译switch语法错误。

此项目运用于我公司商业项目上，功能都没问题，我不太会写文档，如有技术问题可邮件询问：qq3451927565

如何使用?

1.请先去小米官网注册推送账号：https://dev.mi.com/console/appservice/push.html
-------------------

```npm
 cordova plugin add cordova-plugin-mixpush --variable ANDROID_PACKAGE_NAME=你的安卓项目包名  --variable MI_PUSH_APP_IOS_ID=你IOS推送id --variable MI_PUSH_APP_IOS_KEY=你的IOS推送Key
```

或者使用最新的版本
```npm
 cordova plugin add https://github.com/dmcBig/cordova-plugin-mixPush.git --variable ANDROID_PACKAGE_NAME=你的安卓项目包名  --variable MI_PUSH_APP_IOS_ID=你IOS推送id --variable MI_PUSH_APP_IOS_KEY=你的IOS推送Key
```

## Example
code:

       //此代码为最简单用法，详细使用API，请参考www目录下的MixPushPlugin.js 注释
       document.addEventListener('deviceready',push , false);
       function push(){
            var deviceBrand="xiaoMi";//目前只支持小米引擎
            window.plugins.MixPushPlugin.setPushEngine([deviceBrand]);
            var miId = '2882303******08931'; //android id
            var miKey = '57117***2931'; //android key
            //开始启动注册小米推送
            window.plugins.MixPushPlugin.registerPush([miId, miKey]);

            //registerPush事件
           document.addEventListener("MixPushPlugin.onRegisterPush", function onCallBack(data) {
                if (data&&data.code == 200 ) {
                    console.log('注册成功：' + data.regId);
                    setAlias("qq112");//注册成功就设置Alias
                }else{
                    console.log('注册失败：');
                }
            }, false);

            //设置Alias
            function setAlias(alias) {
                window.plugins.MixPushPlugin.setAlias([alias]);
                //onSetAliasPush事件
                document.addEventListener("MixPushPlugin.onSetAliasPush", function onCallBack(data)  {
                    if (data) {
                        data.code == 200 ? "" : setAlias(data.alias);
                        console.log("MixPushPlugin.onSetAliasPush" + data.alias);
                    }
                }, false);
            }

            //来消息后的事件监听
            document.addEventListener("MixPushPlugin.onNotificationArrived",function onCallBack(data) {
                    if (data && data.title) {
                        window.plugins.MixPushPlugin.badgerApplyCount([20]);//来消息后把APP图标上的角标数字改成20
                        console.log('来消息了：' + data.title);
                        alert(data.title);
                    }
             }, false);

            //点消息后的事件监听
             document.addEventListener("MixPushPlugin.onNotificationClicked", function onCallBack(data) {
                         if (data && data.title) {
                             window.plugins.MixPushPlugin.badgerApplyCount([0]);//点消息后清空数字
                             console.log('点消息了：' + data.title);
                             alert(data.title);
                         }
             }, false);

       }


## Android 注意点

如果你采用的是最新的cordova8.0版本，那么当执行 cordova platform add android时候，会默认采用cordova android 7.0 或者更高的版本，由于7.0以上的版本中，Android的目录发生了改变。[参考网址](https://cordova.apache.org/announcements/2017/12/04/cordova-android-7.0.0.html)

cordova android 7.0 运行可能会报错如下：
```
cp: copyFileSync: could not write to dest file (code=ENOENT):/home/ice/WebstormProjects/MyCordova4/platforms/android/res/xml/config.xml

Parsing /home/ice/WebstormProjects/MyCordova4/platforms/android/res/xml/config.xml failed
```

如果想要兼容，可以采用hook的方式，需要两步：

1、新建 hooks/patch-android-studio-check.js 文件,内容如下

```
/**
* This hook overrides a function check at runtime. Currently, cordova-android 7+ incorrectly detects that we are using
* an eclipse style project. This causes a lot of plugins to fail at install time due to paths actually being setup
* for an Android Studio project. Some plugins choose to install things into 'platforms/android/libs' which makes
* this original function assume it is an ecplise project.
*/
module.exports = function (context) {
    if (context.opts.cordova.platforms.indexOf('android') < 0) {
        return;
    }

    const path = context.requireCordovaModule('path');
    const androidStudioPath = path.join(context.opts.projectRoot, 'platforms/android/cordova/lib/AndroidStudio');
    const androidStudio = context.requireCordovaModule(androidStudioPath);
    androidStudio.isAndroidStudioProject = function () { return true; };
};
```

2、在config.xml添加如下代码

```
<platform name="android">
    <hook src="hooks/patch-android-studio-check.js" type="before_plugin_install" />
    <hook src="hooks/patch-android-studio-check.js" type="before_plugin_add" />
    <hook src="hooks/patch-android-studio-check.js" type="before_build" />
    <hook src="hooks/patch-android-studio-check.js" type="before_run" />
    <hook src="hooks/patch-android-studio-check.js" type="before_plugin_rm" />
</platform>
```
此时重新运行 cordova run android 即可正常运行。


## IOS注意点

配置MiSDKRun ：https://dev.mi.com/doc/?p=2995#d5e70 （如果是测试证书测试环境请配置为Debug）

XCODE推送开关：Xcode中的Capabilities中的Push Notifications打开

## 当APP处于后台后如何设置角标  

当APP被用户按了home键处在后台时，JS可能是停止执行的，那来了消息如何设置角标，只需要让后端大兄弟在推送时角标数即可

android
```
new Message.Builder()
.extra("unrecv", "" + unrecv) //unrecv int类型的值

```

ios
```
new Message.IOSBuilder()
.badge(unrecv)//unrecv int类型的值

```







