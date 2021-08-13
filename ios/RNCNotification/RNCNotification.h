//
//  RNCNotification.h
//  RNCNotification
//
//  Created by Kaisavx on 2021/8/13.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import "JPUSHService.h"


NS_ASSUME_NONNULL_BEGIN

@interface RNCNotification : RCTEventEmitter<RCTBridgeModule>

+ (void)registerDeviceToken:(NSData *)deviceToken;
+(void)setup:(NSDictionary *)launchOptions appKey:(NSString*)appKey;
@end

NS_ASSUME_NONNULL_END

//! Project version number for RNCNotification.
FOUNDATION_EXPORT double RNCNotificationVersionNumber;

//! Project version string for RNCNotification.
FOUNDATION_EXPORT const unsigned char RNCNotificationVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <RNCNotification/PublicHeader.h>


