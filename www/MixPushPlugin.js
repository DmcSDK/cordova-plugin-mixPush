cordova.define("cordova-plugin-mixpush.MixPushPlugin", function(require, exports, module) {

//混合推送，目前支持小米，后续会有华为等推送，怎么加后无需任何代码改动就可兼容多种推送
var MixPushPlugin = function() {}


/*设置推送的引擎 xiaoMi||huaWei
  示例：window.plugins.MixPushPlugin.setPushEngine(["xiaoMi"],successCallback,errorCallback);
 */
MixPushPlugin.prototype.setPushEngine = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "setPushEngine", args);
}

/*启动注册推送
示例：let miId = '288230*****896';
      let miKey = '5901***96';
      window.plugins.MixPushPlugin.registerPush([miId, miKey],successCallback,errorCallback); 
 */       
MixPushPlugin.prototype.registerPush = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "registerPush", args);
}

/*退出推送
  示例：window.plugins.MixPushPlugin.exitPush([],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.exitPush = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "exitPush", args);
}

/*为指定用户设置userAccount
  示例：window.plugins.MixPushPlugin.setAccount(["dmc"],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.setAccount = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "setAccount", args);
}

/*取消指定用户的userAccount。
  示例：window.plugins.MixPushPlugin.unsetAccount(["dmc"],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.unsetAccount = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "unsetAccount", args);
}

/*获取客户端的RegId。
  示例：window.plugins.MixPushPlugin.getRegId([],(id)=>{alert(id);}),errorCallback);		
*/
MixPushPlugin.prototype.getRegId = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "getRegId", args);
}

/*为指定用户设置alias
  示例：window.plugins.MixPushPlugin.setAlias(["123"],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.setAlias = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "setAlias", args);
}

/*取消指定用户的alias
  示例：window.plugins.MixPushPlugin.unsetAlias(["123"],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.unsetAlias = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "unsetAlias", args);
}

/*为某个用户设置订阅topic
  示例：window.plugins.MixPushPlugin.subscribe(["112"],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.subscribe = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "subscribe", args);
}

/*取消某个用户的订阅topic
  示例：window.plugins.MixPushPlugin.unsubscribe(["112"],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.unsubscribe = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "unsubscribe", args);
}

/*暂停接收MiPush服务推送的消息
  示例：window.plugins.MixPushPlugin.pausePush([],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.pausePush = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "pausePush", args);
}

/*恢复接收MiPush服务推送的消息，这时服务器会把暂停时期的推送消息重新推送过来
  示例：window.plugins.MixPushPlugin.resumePush([],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.resumePush = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "resumePush", args);
}

/*关闭MiPush推送服务，不再维护长连接，服务不可用
  示例：window.plugins.MixPushPlugin.disablePush([],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.disablePush = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "disablePush", args);
}

/*打开 MiPush 推送服务，继续维护长连接
  示例：window.plugins.MixPushPlugin.enablePush([],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.enablePush = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "enablePush", args);
}

/*清除小米推送弹出的通知
  示例：window.plugins.MixPushPlugin.clearNotification([],successCallback,errorCallback);		
*/
MixPushPlugin.prototype.clearNotification = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "clearNotification", args);
}


MixPushPlugin.prototype.clearNotificationById = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "clearNotificationById", args);
}

//事件======================================================================================//

/*启动注册推送成功失败事件监听
  示例： document.addEventListener("MixPushPlugin.onRegisterPush", (data) => {
            if (data&&data.code == 200 ) {
                console.log('注册成功：' + data.regId);             
            }
        }, false); 	
*/
MixPushPlugin.prototype.onRegisterPush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onRegisterPush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onRegisterPush', data);
}

/*来推送时的监听事件
  示例：document.addEventListener("MixPushPlugin.onNotificationArrived", (data) => {
            if (data && data.title) {
                console.log('来消息了：' + data.title);
            }
        }, false);	
*/
MixPushPlugin.prototype.onNotificationArrived = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onNotificationArrived: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onNotificationArrived', data);
}

/*用来接收服务器发来的通知栏消息（用户点击通知栏时触发）监听事件
  示例：document.addEventListener("MixPushPlugin.onNotificationClicked", (data) => {
            if (data && data.title) {
                console.log('点消息了：' + data.title);
            }
        }, false);	
*/
MixPushPlugin.prototype.onNotificationClicked = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onNotificationClicked: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onNotificationClicked', data);
}

/*设置Alias监听事件
  示例：document.addEventListener("MixPushPlugin.onSetAliasPush", (data) => {
            if (data.code == 200) {
                console.log("成功");
            }
        }, false);	
*/
MixPushPlugin.prototype.onSetAliasPush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onSetAliasPush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onSetAliasPush', data);
}

/*取消Alias监听事件
  示例：document.addEventListener("MixPushPlugin.onUnSetAliasPush", (data) => {
            if (data.code == 200) {
                console.log("成功");
            }
        }, false);	
*/
MixPushPlugin.prototype.onUnSetAliasPush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onUnSetAliasPush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onUnSetAliasPush', data);
}

/*SetAccount监听事件
  示例：用法于上面类似	
*/
MixPushPlugin.prototype.onSetAccountPush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onSetAccountPush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onSetAccountPush', data);
}

/*UnSetAccount监听事件
  示例：用法于上面类似	
*/
MixPushPlugin.prototype.onUnSetAccountPush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onUnSetAccountPush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onUnSetAccountPush', data);
}

/*Subscribe监听事件
  示例：用法于上面类似	
*/
MixPushPlugin.prototype.onSubscribePush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onSubscribePush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onSubscribePush', data);
}

/*UnSubscribe监听事件
  示例：用法于上面类似	
*/
MixPushPlugin.prototype.onUnSubscribePush = function (data) {
  data = JSON.parse(JSON.stringify(data));
  console.log('MixPushPlugin:onUnSubscribePush: ' + data);
  cordova.fireDocumentEvent('MixPushPlugin.onUnSubscribePush', data);
}



/*设置APP角标数字
  示例：设置数字20当设置0的时候就等同于badgerRemoveCount，window.plugins.MixPushPlugin.badgerApplyCount([20],successCallback,errorCallback);
 */
MixPushPlugin.prototype.badgerApplyCount = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "badgerApplyCount", args);
}

//清空APP角标数字
//示例：window.plugins.MixPushPlugin.badgerRemoveCount([],successCallback,errorCallback);
MixPushPlugin.prototype.badgerRemoveCount = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "badgerRemoveCount", args);
}


MixPushPlugin.prototype.addBadgerToSp = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "addBadgerToSp", args);
}


MixPushPlugin.prototype.minusBadgerToSp = function(args,successCallback,errorCallback) {
    cordova.exec(successCallback, errorCallback, 'MixPushPlugin', "minusBadgerToSp", args);
}

if (!window.plugins) {
    window.plugins = {}
}

if (!window.plugins.MixPushPlugin) {
    window.plugins.MixPushPlugin = new MixPushPlugin()
}


module.exports = new MixPushPlugin()

});
