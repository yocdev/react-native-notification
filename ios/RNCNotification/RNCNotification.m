//
//  RNCNotification.m
//  react-native-notification
//
//  Created by Kaisavx on 2021/8/12.
//

#import "RNCNotification.h"
#import <dispatch/dispatch.h>
// 引入 JPush 功能所需头文件
#import "JPUSHService.h"
#import <PushKit/PushKit.h>
// iOS10 注册 APNs 所需头文件
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif

@interface RNCNotification()<JPUSHRegisterDelegate>
@property (nonatomic,strong) NSString*registrationID;
@end

@implementation RNCNotification

RCT_EXPORT_MODULE();
static NSDictionary* remoteNotification = nil;
+ (void)registerDeviceToken:(NSData *)deviceToken{
    [JPUSHService registerDeviceToken:deviceToken];
}

- (void)networkDidReceiveMessage:(NSNotification *)notification {
        NSDictionary * userInfo = [notification userInfo];
        NSString *content = [userInfo valueForKey:@"content"];
        NSString *messageID = [userInfo valueForKey:@"_j_msgid"];
        NSDictionary *extras = [userInfo valueForKey:@"extras"];
        NSString *customizeField1 = [extras valueForKey:@"customizeField1"]; //服务端传递的 Extras 附加字段，key 是自己定义的
  NSLog(@"networkDidReceiveMessage content:%@ messageID:%@ extras:%@ extras:%@",content,messageID,extras,customizeField1);
    }

- (NSArray<NSString *> *)supportedEvents
{
  return @[
      @"register",
      @"notification",
      @"openNotification",
  ];
}

+(void)setup:(NSDictionary *)launchOptions appKey:(NSString*)appKey{
    [JPUSHService setupWithOption:launchOptions
                           appKey:appKey
                            channel:@"Publish channel"
                   apsForProduction:NO
              advertisingIdentifier:nil];
    remoteNotification = [launchOptions objectForKey: UIApplicationLaunchOptionsRemoteNotificationKey];
//    NSLog(@"setup:%@",remoteNotification);
   
}

// iOS 12 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center openSettingsForNotification:(UNNotification *)notification{
  if (notification && [notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    //从通知界面直接进入应用
    NSLog(@"openSettingsForNotification surface to app");
  }else{
    //从通知设置界面进入应用
    NSLog(@"openSettingsForNotification option to app");
  }
}

// iOS 10 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(NSInteger))completionHandler {
  NSLog(@"JIGUANG willPresentNotification ");
  // Required
  NSDictionary * userInfo = notification.request.content.userInfo;
  if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    [JPUSHService handleRemoteNotification:userInfo];
  }
  completionHandler(UNNotificationPresentationOptionAlert); // 需要执行这个方法，选择是否提醒用户，有 Badge、Sound、Alert 三种类型可以选择设置
    
    NSDictionary *aps = [userInfo valueForKey:@"aps"];
    NSString *content = [aps valueForKey:@"alert"]; //推送显示的内容
    NSNumber* badge = [aps valueForKey:@"badge"] ; //badge 数量
    NSString *sound = [aps valueForKey:@"sound"]; //播放的声音
    NSString *messageID = [userInfo valueForKey:@"_j_msgid"];
    NSString *body = [content valueForKey:@"body"];
    NSString *title = [content valueForKey:@"title"];
      // 取得 Extras 字段内容
    NSString *customizeField1 = [userInfo valueForKey:@"customizeExtras"]; //服务端中 Extras 字段，key 是自己定义的
    NSLog(@"JIGUANG willPresentNotification content:%@ badge:%d sound:%@ messageID:%@ extras:%@",
          content,
          badge,
          sound,
          messageID,
          customizeField1);
    [self
     sendEventWithName:@"notification"
     body:@{
         @"messageID":messageID,
         @"title":title,
         @"body":body,
         @"badge":badge,
     }];
}

// iOS 10 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler {
  NSLog(@"JIGUANG didReceiveNotificationResponse");
  // Required
  NSDictionary * userInfo = response.notification.request.content.userInfo;
  if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    [JPUSHService handleRemoteNotification:userInfo];
  }
  NSDictionary *aps = [userInfo valueForKey:@"aps"];
    NSString *content = [aps valueForKey:@"alert"]; //推送显示的内容
    NSNumber* badge = [aps valueForKey:@"badge"] ; //badge 数量
    NSString *sound = [aps valueForKey:@"sound"]; //播放的声音
    NSString *messageID = [userInfo valueForKey:@"_j_msgid"];
    NSString *body = [content valueForKey:@"body"];
    NSString *title = [content valueForKey:@"title"];
    // 取得 Extras 字段内容
    NSString *customizeField1 = [userInfo valueForKey:@"customizeExtras"]; //服务端中 Extras 字段，key 是自己定义的
    
  NSLog(@"JIGUANG didReceiveNotificationResponse  content:%@ messageID:%@ extras:%@",content,messageID,customizeField1);
    [self
     sendEventWithName:@"openNotification"
     body:@{
         @"messageID":messageID,
         @"title":title,
         @"body":body,
         @"badge":badge,
     }];
  completionHandler();  // 系统要求执行这个方法
}

- (void)jpushNotificationAuthorization:(JPAuthorizationStatus)status withInfo:(NSDictionary *)info{
  NSLog(@"JIGUANG receive notification authorization status:%lu, info:%@", status, info);
}

RCT_REMAP_METHOD(init,
                 initWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject){
    NSLog(@"init");
    JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
      entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound|JPAuthorizationOptionProvidesAppNotificationSettings;
      if ([[UIDevice currentDevice].systemVersion floatValue] >= 8.0) {
        // 可以添加自定义 categories
        // NSSet<UNNotificationCategory *> *categories for iOS10 or later
        // NSSet<UIUserNotificationCategory *> *categories for iOS8 and iOS9
      }
    dispatch_async(dispatch_get_main_queue(), ^{
        [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
    });
    
  //  [self voipRegistration];
    
    [JPUSHService registrationIDCompletionHandler:^(int resCode, NSString *registrationID) {
        NSLog(@"resCode : %d,registrationID: %@",resCode,registrationID);
        self.registrationID = [registrationID copy];
        NSString*str=@"";
        if(self.registrationID!= nil){
            str = self.registrationID;
        }
        [self sendEventWithName:@"register" body:@{
            @"type":@"ios",
            @"registrationID":str,
//            @"notification":[NSString stringWithFormat:@"%@",remoteNotification]
        }];
        if(remoteNotification!=nil){
            NSDictionary *aps = [remoteNotification valueForKey:@"aps"];
            NSString *messageID = [remoteNotification valueForKey:@"_j_msgid"];
            NSString *content = [aps valueForKey:@"alert"]; //推送显示的内容
            NSNumber* badge = [aps valueForKey:@"badge"] ;
            NSString *body = [content valueForKey:@"body"];
            NSString *title = [content valueForKey:@"title"];
            [self sendEventWithName:@"openNotification" body:@{
                @"messageID":messageID,
                @"title":title,
                @"body":body,
                @"badge":badge,
            }];
        }
        
        
    }];
    NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
    [defaultCenter addObserver:self selector:@selector(networkDidReceiveMessage:) name:kJPFNetworkDidReceiveMessageNotification object:nil];
    resolve(@{});
}


RCT_REMAP_METHOD(getRegistrationId,
                 getRegistrationIdWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    NSLog(@"getRegistrationId");
    NSString*str=@"";
    if(self.registrationID!= nil){
        str = self.registrationID;
    }
    resolve(@{@"type":@"ios",@"registrationID":str});
}

RCT_REMAP_METHOD(removeAllNotifications,
                 removeAllNotificationsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    NSLog(@"removeAllNotifications");
    [JPUSHService removeNotification:nil];
    resolve(@{});
//    resolve(@{@"type":@"ios",@"registrationID":self.registrationID});
}

RCT_REMAP_METHOD(setBadge,
                 setBadge:(NSInteger)value
                 Resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    NSLog(@"setBadge");
    [JPUSHService setBadge:value];
    resolve(@{});
//    resolve(@{@"type":@"ios",@"registrationID":self.registrationID});
}



@end
