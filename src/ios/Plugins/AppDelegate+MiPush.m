//
//  AppDelegate+MiPush.m
//

#import "AppDelegate.h"
#import "MainViewController.h"
#import "AppDelegate+MiPush.h"
#import "MiPushPlugin.h"

@implementation AppDelegate (MiPush)
static AppDelegate *myAppDeleagate=nil;
- (BOOL)application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions
{
    
    // 使用本地通知 (本例中只是badge，但是还有alert和sound都属于通知类型,其实如果只进行未读数在appIcon显示,只需要badge就可, 这里全写上为了方便以后的使用)
    UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert categories:nil];
    // 进行注册
    [application registerUserNotificationSettings:settings];
    myAppDeleagate=self;
    // [MiPushSDK registerMiPush:self type:0 connect:YES];
    // 点击通知打开app处理逻辑
    NSDictionary* userInfo = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    if(userInfo){
        NSLog(@"点了%@", userInfo);
        [MiPushPlugin onNotificationMessageClickedCallBack:userInfo];
    }
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

+(AppDelegate *)delegate{
    
    return  myAppDeleagate;
}

#pragma mark Local And Push Notification
//如果当通知到达的时候，你的应用已经在运行，对于 iOS10 以下
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    NSLog(@"IOS 线路: %@", userInfo);
    // 当同时启动APNs与内部长连接时, 把两处收到的消息合并. 通过miPushReceiveNotification返回
    [MiPushSDK handleReceiveRemoteNotification:userInfo];
}


// iOS10新加入的回调方法
// 应用在前台收到通知
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
    NSDictionary * userInfo = notification.request.content.userInfo;
    if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [MiPushSDK handleReceiveRemoteNotification:userInfo];
    }
    //    completionHandler(UNNotificationPresentationOptionAlert);
}

// 点击通知进入应用
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler {
    NSDictionary * userInfo = response.notification.request.content.userInfo;
    if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [MiPushSDK handleReceiveRemoteNotification:userInfo];
    }
    completionHandler();
}


- (void)miPushReceiveNotification:(NSDictionary*)data
{
    // 1.当启动长连接时, 收到消息会回调此处
    // 2.[MiPushSDK handleReceiveRemoteNotification]
    //   当使用此方法后会把APNs消息导入到此
    NSLog(@"小米统一消息：%@", data);
    [MiPushPlugin onNotificationMessageArrivedCallBack:data];
}


#pragma mark MiPushSDKDelegate
- (void)miPushRequestSuccWithSelector:(NSString *)selector data:(NSDictionary *)data
{
    NSLog(@"command succ(%@): %@",  selector, data);
    [self getOperateType:selector code:200 date:data];
}

- (void)miPushRequestErrWithSelector:(NSString *)selector error:(int)error data:(NSDictionary *)data
{
    [self getOperateType:selector code:500 date:data];
    NSLog(@"command error(%@): %@",  selector, data);
}

- (void)getOperateType:(NSString*)selector code:(int) code date:(NSDictionary *)data
{
    @try {
 
    NSNumber *mCode=[NSNumber numberWithInt:code];
    NSString *ret = nil;
    if ([selector hasPrefix:@"registerMiPush:"] ) {
        ret = @"客户端注册设备";
        //这地方还不能获取到regid
    }else if ([selector isEqualToString:@"unregisterMiPush"]) {
        ret = @"客户端设备注销";
    }else if ([selector isEqualToString:@"registerApp"]) {
        ret = @"注册App";
    }else if ([selector isEqualToString:@"bindDeviceToken:"]) {
        ret = @"绑定 PushDeviceToken";
        [MiPushPlugin callbackWithType:@"onRegisterPush" data:@{@"regid": data[@"regid"],@"code":mCode}];
    }else if ([selector isEqualToString:@"setAlias:"]) {
        ret = @"客户端设置别名";
        [MiPushPlugin callbackWithType:@"onSetAliasPush" data:@{@"alias": data[@"alias"],@"code":mCode}];
    }else if ([selector isEqualToString:@"unsetAlias:"]) {
        ret = @"客户端取消别名";
        [MiPushPlugin callbackWithType:@"onUnSetAliasPush" data:@{@"alias": data[@"alias"],@"code":mCode}];
    }else if ([selector isEqualToString:@"subscribe:"]) {
        ret = @"客户端设置主题";
        [MiPushPlugin callbackWithType:@"onSubscribePush" data:@{@"topic": data[@"topic"],@"code":mCode}];
    }else if ([selector isEqualToString:@"unsubscribe:"]) {
        ret = @"客户端取消主题";
        [MiPushPlugin callbackWithType:@"onUnSubscribePush" data:@{@"topic": data[@"topic"],@"code":mCode}];
    }else if ([selector isEqualToString:@"setAccount:"]) {
        ret = @"客户端设置账号";
        [MiPushPlugin callbackWithType:@"onSetAccountPush" data:@{@"account": data[@"account"],@"code":mCode}];
    }else if ([selector isEqualToString:@"unsetAccount:"]) {
        ret = @"客户端取消账号";
        [MiPushPlugin callbackWithType:@"onUnSetAccountPush" data:@{@"account": data[@"account"],@"code":mCode}];
    }else if ([selector isEqualToString:@"openAppNotify:"]) {
        ret = @"统计客户端";
    }else if ([selector isEqualToString:@"getAllAliasAsync"]) {
        ret = @"获取Alias设置信息";
        for (NSString *key in data[@"list"]) {
           [MiPushSDK unsetAlias:key];
        }
        
    }else if ([selector isEqualToString:@"getAllTopicAsync"]) {
        ret = @"获取Topic设置信息";
        for (NSString *key in data[@"list"]) {
            [MiPushSDK unsubscribe:key];
        }
    }else if ([selector isEqualToString:@"getRegId"]) {
        ret = @"获取getRegId信息";
        
    }else if ([selector isEqualToString:@"getAllAccount"]) {
        ret = @"获取getAllAccount信息";
        for (NSString *key in data[@"list"]) {
            [MiPushSDK unsetAccount:key];
        }
    }
   
    } @catch (NSException *exception) {
        NSLog(@"PUSH 有错");
    }

}

#pragma mark 注册push服务.
- (void)application:(UIApplication *)app didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    NSString *deviceTokenStr = [NSString stringWithFormat:@"APNS token: %@", [deviceToken description]];

    NSLog(@"注册APNS成功 %@",deviceTokenStr);
    // 注册APNS成功, 注册deviceToken  <aba9ee57 691e7f73 6839a4fb 29dbbb6b 9eddabbb 847f49a6 2ae2f6a8 9660f0ae>
    [MiPushSDK bindDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)app didFailToRegisterForRemoteNotificationsWithError:(NSError *)err
{
    NSLog(@"注册APNS失败: %@", err);
    //[MiPushSDK registerMiPush:self type:0 connect:YES];
    // 注册APNS失败.
    // 自行处理.
}

@end
