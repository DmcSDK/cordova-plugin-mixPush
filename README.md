# cordova-plugin-mixPush
Cordova 推送，支持设置角标，事件回调。
此项目代码设计以混合推送为基础，兼容多种推送引擎。

此项目运用于我公司商业项目上，功能都没问题，我不太会写文档，如有技术问题可邮件询问：911512152@qq.com

如何使用?

1.请先去小米官网注册推送账号：https://dev.mi.com/console/appservice/push.html
-------------------

```npm
 cordova plugin add cordova-plugin-mixpush --variable ANDROID_PACKAGE_NAME=你的安卓项目包名  --variable MI_PUSH_APP_IOS_ID=你IOS推送id --variable MI_PUSH_APP_IOS_KEY=你的IOS推送Key
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

## IOS注意点

配置MiSDKRun ：https://dev.mi.com/doc/?p=2995#d5e70 （如果是测试证书测试环境请配置为Debug）

XCODE推送开关：Xcode中的Capabilities中的Push Notifications打开









