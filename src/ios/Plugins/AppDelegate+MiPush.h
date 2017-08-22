//
//  AppDelegate+MiPush.h
//  delegateExtention
//
//  Created by wenin819 on 17/3/26.
//

#import "AppDelegate.h"
#import "MiPushSDK.h"

@interface AppDelegate (MiPush)
        <MiPushSDKDelegate,
        UIApplicationDelegate,
        UNUserNotificationCenterDelegate>
+(AppDelegate *)delegate;
@end
