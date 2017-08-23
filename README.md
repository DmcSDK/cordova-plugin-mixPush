# cordova-plugin-mixPush
android  mediaPicker support  selection of multiple image and video 

如何使用?
-------------------

use npm:

```npm
  cordova plugin add cordova-plugin-mediapicker-dmcbig
```
## Example
code:
       //此为代码为最简单用法，详细使用API，请参考www目录下的MixPushPlugin.js 注释
       document.addEventListener('deviceready',push , false);

       function push(){
            var deviceBrand="xiaoMi";//目前只支持小米引擎
            window.plugins.MixPushPlugin.setPushEngine([deviceBrand]);
            var miId = '2882303761517608931';
            var miKey = '5711760892931';
            window.plugins.MixPushPlugin.registerPush([miId, miKey]);

            //registerPush事件
           document.addEventListener("MixPushPlugin.onRegisterPush", function onCallBack(data) {
                if (data&&data.code == 200 ) {
                    console.log('注册成功：' + data.regId);
                    setAlias("qq112");
                }else{
                    console.log('注册失败：');
                }
            }, false);

            function setAlias(alias) {
                window.plugins.MixPushPlugin.setAlias([alias]);
                //onSetAliasPush事件
                document.addEventListener("MixPushPlugin.onSetAliasPush", function onCallBack(data)  {
                    if (data) {
                        data.code == 200 ? "" : setAlias(data.alias);
                        console.log("MixPushPlugin.onSetAliasPush" + data.code);
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







