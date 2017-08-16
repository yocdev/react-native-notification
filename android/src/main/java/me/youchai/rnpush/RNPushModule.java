
package me.youchai.rnpush;

import android.content.Context;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import me.youchai.rnpush.utils.Logger;

public class RNPushModule extends ReactContextBaseJavaModule {

  private static String TAG = "RNPushModule";
  private PushService pushService = null;
  private static ReactApplicationContext __rac;

  public RNPushModule(ReactApplicationContext reactContext) {
    super(reactContext);
    __rac = reactContext;
  }

  @Override
  public boolean canOverrideExistingModule() {
    return true;
  }

  @Override
  public String getName() {
    return "RNPush";
  }

  public static void sendEvent(String key, WritableMap event) {
    Logger.i("sending event");
    __rac.getJSModule(RCTNativeAppEventEmitter.class).emit(key, event);
  }

  @ReactMethod
  public void init(ReadableMap configs) {
    ReadableMap config = null;
    if (configs.hasKey("android")) {
      config = configs.getMap("android");
    }
    this.pushService = PushServiceFactory.create(__rac, config);
    this.pushService.init();
    Logger.i("init Success!");
  }

  @ReactMethod
  public void stop() {
    if (pushService == null) {
      return;
    }
    pushService.stop();
    Logger.i("Stop push");
  }

  @ReactMethod
  public void resume() {
    if (pushService == null) {
      return;
    }
    pushService.resume();
    Logger.i("Resume push");
  }

  @ReactMethod
  public void clearBadge() {
    Logger.i("clear badge");
    // not working on android
  }

  /**
   * Get registration id, different from RNPushModule.addGetRegistrationIdListener, this
   * method has no calling limits.
   *
   * @param callback callback with registrationId
   */
  @ReactMethod
  public void getRegistrationId(Promise promise) {
    if (pushService == null) {
      promise.reject("pushService not initialized");
    }
    try {
      WritableMap r = Arguments.createMap();
      r.putString("type", pushService.getName());
      r.putString("registrationId", pushService.getRegistrationId());
      promise.resolve(r);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e.getMessage());
    }
  }

  /**
   * Clear all notifications, suggest invoke this method while exiting app.
   */
  @ReactMethod
  public void clearAllNotifications(Promise promise) {
    if (pushService == null) {
      promise.reject("pushService not initialized");
      return;
    }
    try {
      pushService.clearAllNotification();
      promise.resolve(true);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e.getMessage());
    }
  }

  /**
   * Clear specified notification
   *
   * @param id the notification id
   */
  // @ReactMethod
  public void clearNotificationById(String id, Promise promise) {
    if (pushService == null) {
      promise.reject("pushService not initialized");
      return;
    }
    try {
      pushService.clearNotificationById(id);
      promise.resolve(true);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e.getMessage());
    }
  }

}
