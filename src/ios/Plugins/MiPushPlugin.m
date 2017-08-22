//
//  Created by wenin819 on 17-03-24.
//

#import "MiPushPlugin.h"
#import "MiPushSDK.h"

@implementation MiPushPlugin

- (void)pluginInitialize {
    [super pluginInitialize];
    SharedMiPushPlugin = self;
}

// 注册小米推送
- (void)registerPush:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        [MiPushSDK registerMiPush:[AppDelegate delegate] type:0 connect:YES];
        NSLog(@"启动小米推送");
    }];
}

- (void)exitPush:(CDVInvokedUrlCommand *)command {
    NSLog(@"退出小米推送");
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSLog(@"原生注销push");
            [MiPushSDK unregisterMiPush];//注意：iOS10之后此操作会导致一段时间无法重新注册设备。
        }else{
            [MiPushSDK getAllAliasAsync];
            [MiPushSDK getAllTopicAsync];
            [MiPushSDK getAllAccountAsync];
        }
    }];
}


- (void)setAccount:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *account = [command argumentAtIndex:0];
            [MiPushSDK setAccount:account];
        }
    }];
}


- (void)unsetAccount:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *account = [command argumentAtIndex:0];
            [MiPushSDK unsetAccount:account];
        }
    }];
}

- (void)getAllTopic:(CDVInvokedUrlCommand *)command {
    getAllTopic_command=command;
    [self.commandDelegate runInBackground:^{
        [MiPushSDK getAllTopicAsync];
    }];
}


- (void)getAllAlias:(CDVInvokedUrlCommand *)command {
    getAllAlias_command=command;
    [self.commandDelegate runInBackground:^{
        [MiPushSDK getAllAliasAsync];
    }];
}


- (void)getRegId:(CDVInvokedUrlCommand *)command {
    getRegId_command=command;
    [self.commandDelegate runInBackground:^{
        [MiPushSDK getRegId];
    }];
}


- (void)getAllAccount:(CDVInvokedUrlCommand *)command {
    getAllAccount_command=command;
    [self.commandDelegate runInBackground:^{
        [MiPushSDK getAllAccountAsync];
    }];
}

//以下为js中可调用接口
//设置标签、别名
- (void)setAlias:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *alias = [command argumentAtIndex:0];
            [MiPushSDK setAlias:alias];
        }
    }];
}

- (void)unsetAlias:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *alias = [command argumentAtIndex:0];
            [MiPushSDK unsetAlias:alias];
        }
    }];
}

// 订阅topic
- (void)subscribe:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *topic = [command argumentAtIndex:0];
            [MiPushSDK subscribe:topic];
        }
    }];
}

- (void)unsubscribe:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *topic = [command argumentAtIndex:0];
            [MiPushSDK unsubscribe:topic];
        }
    }];
}

-(void) clearNotification:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSInteger nub=[UIApplication sharedApplication].applicationIconBadgeNumber;
        [UIApplication sharedApplication].applicationIconBadgeNumber =0;
        [[UIApplication sharedApplication]cancelAllLocalNotifications];
        [UIApplication sharedApplication].applicationIconBadgeNumber =nub;
    }];
}

-(void) clearNotificationById:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        [[UIApplication sharedApplication]cancelAllLocalNotifications];//暂未实现
    }];
}

// 接受到消息
+ (void)onNotificationMessageArrivedCallBack:(NSDictionary *)data {
    [self callbackWithType: @"onNotificationArrived" data:data];
}


// 用户点击
+ (void)onNotificationMessageClickedCallBack:(NSDictionary *)data {
    [self callbackWithType: @"onNotificationClicked" data:data];
}

// 小米推送注册成功
+ (void)onReceiveRegisterResultCallBack:(NSString *)regId {
    NSLog(@"regid = %@", regId);
    [self callbackWithType: @"onRegisterPush" data:@{@"regId": regId}];
}

-(void)badgerApplyCount:(CDVInvokedUrlCommand*)command{
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSNumber *number = [command argumentAtIndex:0];
            NSInteger n=[number integerValue];
            [UIApplication sharedApplication].applicationIconBadgeNumber =n;
        }
    }];
}

-(void)badgerRemoveCount:(CDVInvokedUrlCommand*)command{
    [self.commandDelegate runInBackground:^{
        [UIApplication sharedApplication].applicationIconBadgeNumber =0;
        [[UIApplication sharedApplication]cancelAllLocalNotifications];
    }];
}

-(void)badgerMinusCount:(CDVInvokedUrlCommand*)command{
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSNumber *number = [command argumentAtIndex:0];
            NSInteger n=[number integerValue];
            [UIApplication sharedApplication].applicationIconBadgeNumber -=n;
        }
    }];
}



+ (void)callbackWithType: (NSString *) type data:(NSDictionary *)data {
    NSString *json = nil;
    NSError *error = nil;

    @try {
        if(data[@"aps"]) {
            NSDictionary *alertData =  [NSDictionary dictionaryWithObject:data[@"aps"][@"alert"] forKey:@"alert"];
            if(alertData) {
                NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] initWithDictionary:data];
                [dictionary addEntriesFromDictionary:alertData];
                data = dictionary;
            }
        }
        
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&error];
        
        if (error != nil) {
            NSLog(@"NSDictionary JSONString error: %@", [error localizedDescription]);
        } else {
            json = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        }
    } @catch (NSException *exception) {
        
    }

    
    NSLog(@"消息：%@",json);
    NSString *js = [NSString stringWithFormat:@"window.plugins.MixPushPlugin.%@(%@)", type, json];
      NSLog(@"消息JS：%@",js);
    if (SharedMiPushPlugin) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [SharedMiPushPlugin.commandDelegate evalJs:js];
        });
    } else {
        [NSTimer scheduledTimerWithTimeInterval:0.1 repeats:YES block:^(NSTimer * _Nonnull timer) {
            if(!SharedMiPushPlugin) {
                return;
            }
            [timer invalidate];
            dispatch_async(dispatch_get_main_queue(), ^{
                [SharedMiPushPlugin.commandDelegate evalJs:js];
            });
        }];
    }
}


@end
