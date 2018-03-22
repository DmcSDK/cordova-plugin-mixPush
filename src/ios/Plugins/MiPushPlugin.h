

#import <Cordova/CDV.h>
#import "AppDelegate+MiPush.h"

@interface MiPushPlugin : CDVPlugin{
    CDVInvokedUrlCommand * getAllTopic_command;
    CDVInvokedUrlCommand * getAllAlias_command;
    CDVInvokedUrlCommand * getAllAccount_command;
    CDVInvokedUrlCommand * getRegId_command;
    CDVPluginResult *pluginResult;
}

// 注册小米推送
-(void)registerPush:(CDVInvokedUrlCommand*)command;

-(void)exitPush:(CDVInvokedUrlCommand*)command;

// 设置userAccount
-(void)setAccount:(CDVInvokedUrlCommand*)command;
-(void)unsetAccount:(CDVInvokedUrlCommand*)command;



// 设置标签、别名
-(void)setAlias:(CDVInvokedUrlCommand*)command;
-(void)unsetAlias:(CDVInvokedUrlCommand*)command;


// 订阅topic
-(void)subscribe:(CDVInvokedUrlCommand*)command;
-(void)unsubscribe:(CDVInvokedUrlCommand*)command;
-(void) clearNotification:(CDVInvokedUrlCommand *)command;

- (void)getAllTopic:(CDVInvokedUrlCommand *)command;
- (void)getAllAlias:(CDVInvokedUrlCommand *)command;
- (void)getAllAccount:(CDVInvokedUrlCommand *)command;
- (void)getRegId:(CDVInvokedUrlCommand *)command;
// 接受到消息
+(void)onNotificationMessageArrivedCallBack:(NSDictionary*)data;
// 用户点击
+(void)onNotificationMessageClickedCallBack:(NSDictionary*)data;
// 小米推送注册成功
+(void)onReceiveRegisterResultCallBack:(NSString*)regId;

-(void)badgerApplyCount:(CDVInvokedUrlCommand*)command;
-(void)badgerRemoveCount:(CDVInvokedUrlCommand*)command;
-(void)badgerMinusCount:(CDVInvokedUrlCommand*)command;

+(void)callbackWithType: (NSString *) type data:(NSDictionary *)data;
@end

MiPushPlugin *SharedMiPushPlugin;
