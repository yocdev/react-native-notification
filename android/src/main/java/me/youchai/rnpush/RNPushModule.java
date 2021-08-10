package me.youchai.rnpush;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import java.util.Collections;

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

  public static void onRegister(String type, String registrationId) {
    WritableMap map = Arguments.createMap();
    map.putString("type", type);
    map.putString("registrationId", registrationId);

    RNPushModule.sendEvent("register", map);
  }

  public static void onRegisterError(String message) {
    WritableMap map = Arguments.createMap();
    map.putString("message", message);

    RNPushModule.sendEvent("registrationError", map);
  }

  public static void onNotification(Notification note) {
    RNPushModule.sendEvent("notification", note.toWritableMap());
  }

  public static void onNotificationClick(Notification note) {
    PushService.setInitialNotification(note);
  }

  public static void sendEvent(String key, WritableMap event) {
    if (__rac != null) {
      RCTNativeAppEventEmitter emitter =
        __rac.getJSModule(RCTNativeAppEventEmitter.class);
      if (emitter != null) {
        emitter.emit(key, event);
      }
    }
  }

  @ReactMethod
  public void init(ReadableMap configs, Promise promise) {
    ReadableMap config = null;
    if (configs != null && configs.hasKey("android")) {
      config = configs.getMap("android");
    }
    try {
      this.pushService = PushServiceFactory.create(__rac, config);
      this.pushService.init();
      Logger.i("init Success!");
      promise.resolve(null);
    } catch (Throwable e) {
      Logger.i("error when init push service" + e.getMessage());
      promise.reject(e);
    }
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
  public void getRegistrationId(Promise promise) {
    if (pushService == null) {
      promise.reject(TAG,"pushService not initialized");
    }
    try {
      WritableMap r = Arguments.createMap();
      r.putString("type", pushService.getName());
      r.putString("registrationId", pushService.getRegistrationId());
      promise.resolve(r);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e);
    }
  }

  @ReactMethod
  public void getInitialNotification(Promise promise) {
    Notification note = PushService.getInitialNotification();
    if (note != null) {
      promise.resolve(note.toWritableMap());
      PushService.setInitialNotification(null);
    } else {
      promise.resolve(null);
    }
  }

  @ReactMethod
  public void scheduleLocalNotification(ReadableMap args, Promise promise) {
    if (pushService == null) {
      promise.reject(TAG,"pushService not initialized");
    }
    Notification note = new Notification();
    if (args.hasKey("title")) {
      note.setTitle(args.getString("title"));
    }
    if (args.hasKey("content")) {
      note.setContent(args.getString("content"));
    }
    if (args.hasKey("extras")) {
      note.setExtras(args.getString("extras"));
    }
    if (args.hasKey("fireDate")) {
      note.setFireDate((long) args.getDouble("fireDate"));
    }
    Logger.i(args.toString());
    try {
      pushService.scheduleLocalNotification(note);
      promise.resolve(null);
    } catch (Throwable e) {
      Logger.i(e.getMessage());
      promise.reject(e);
    }
  }

  @ReactMethod
  public void cancelLocalNotifications(String id, Promise promise) {
    if (pushService == null) {
      promise.reject(TAG,"pushService not initialized");
    }
    try {
      pushService.cancelLocalNotifications(Collections.singletonList(id));
      promise.resolve(null);
    } catch (Throwable e) {
      Logger.i(e.getMessage());
      promise.reject(e);
    }
  }

  @ReactMethod
  public void cancelAllLocalNotifications(Promise promise) {
    if (pushService == null) {
      promise.reject(TAG,"pushService not initialized");
    }
    try {
      pushService.cancelAllLocalNotifications();
      promise.resolve(null);
    } catch (Throwable e) {
      Logger.i(e.getMessage());
      promise.reject(e);
    }
  }

  @ReactMethod
  public void removeAllNotifications(Promise promise) {
    if (pushService == null) {
      promise.reject(TAG, "pushService not initialized");
      return;
    }
    try {
      pushService.removeAllNotifications();
      promise.resolve(true);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e);
    }
  }

  @ReactMethod
  public void removeNotifications(String id, Promise promise) {
    if (pushService == null) {
      promise.reject(TAG, "pushService not initialized");
      return;
    }
    try {
      pushService.removeNotifications(Collections.singletonList(id));
      promise.resolve(true);
    } catch (Exception e) {
      e.printStackTrace();
      promise.reject(e);
    }
  }
}
