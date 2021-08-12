//
//  RNCNotification.m
//  react-native-notification
//
//  Created by Kaisavx on 2021/8/12.
//

#import "RNCNotification.h"

@implementation RNCNotification

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"notification"];
}



RCT_REMAP_METHOD(getRegistrationId,
                 getRegistrationIdByCode:(int)code
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    if(code>0){
        resolve(@{@"code":[NSString stringWithFormat:@"%d",code]});
        [self sendEventWithName:@"notification" body:@{@"code": [NSString stringWithFormat:@"%d",code]}];
    }else{
        NSError * err=[NSError errorWithDomain:@"test" code:0 userInfo:nil];
        reject(@"getRegistrationId",@"error",err);
    }
}

@end
