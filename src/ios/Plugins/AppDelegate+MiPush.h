

#import "AppDelegate.h"
#import "MiPushSDK.h"

@interface AppDelegate (MiPush)
        <MiPushSDKDelegate,
        UIApplicationDelegate,
        UNUserNotificationCenterDelegate>
+(AppDelegate *)delegate;
@end
