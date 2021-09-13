package me.youchai.rnpush;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.youchai.rnpush.utils.Logger;

public class RNPushModule extends ReactContextBaseJavaModule {

  private static String TAG = "RNPushModule";
  private PushService pushService = null;
  private static ReactApplicationContext __rac;

  enum EventType {
    REGISTER("register"),
    REGISTER_ERROR("registrationError"),
    NOTIFICATION("notification"),
    OPEN_NOTIFICATION("openNotification"),
    NOTIFICATION_AUTHORIZATION("notificationAuthorization");

    public String value;

    private EventType(String value) {
      this.value = value;
    }
  }

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

    RNPushModule.sendEvent(EventType.REGISTER.value, map);
  }

  public static void onRegisterError(String message) {
    WritableMap map = Arguments.createMap();
    map.putString("message", message);

    RNPushModule.sendEvent(EventType.REGISTER_ERROR.value, map);
  }

  public static void onNotification(Notification note) {
    Log.d(TAG, "onNotification");
    RNPushModule.sendEvent(EventType.NOTIFICATION.value, note.toWritableMap());
  }

  public static void onNotificationClick(Notification note) {
    Log.d(TAG, "onNotificationClick");
//    PushService.setInitialNotification(note);
    RNPushModule.sendEvent(EventType.OPEN_NOTIFICATION.value, note.toWritableMap());
  }

  public static void onNotificationAuthorization(ReactApplicationContext reactContext) {
    Log.d(TAG, "onNotificationAuthorization");
    boolean isOn = NotificationManagerCompat.from(reactContext).areNotificationsEnabled();
    WritableMap map = Arguments.createMap();
    map.putBoolean("state", isOn);
    RNPushModule.sendEvent(EventType.NOTIFICATION_AUTHORIZATION.value, map);
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

  private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      String description = getString(R.string.channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannelGroup customerGroup = new NotificationChannelGroup("TODO", "企业给员工的任务");
      NotificationChannelGroup todoGroup = new NotificationChannelGroup("Customer Dynamics", "客户动态");

      NotificationChannel customerChannel = new NotificationChannel("Customer Dynamics", "客户动态通知", importance);
      customerChannel.setGroup(customerGroup.getId());
      NotificationChannel hignChannel = new NotificationChannel("high_system", "服务提醒", importance);
      hignChannel.setGroup(customerGroup.getId());
      NotificationChannel todoChannel = new NotificationChannel("TODO", "企业给员工的任务", importance);
      todoChannel.setGroup(todoGroup.getId());
      //      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = __rac.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannelGroups(Arrays.asList(customerGroup, todoGroup));
      notificationManager.createNotificationChannels(Arrays.asList(customerChannel, hignChannel,todoChannel));

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
      RNPushModule.onNotificationAuthorization(__rac);

      Logger.i("init Success!");
      promise.resolve(null);
      
      createNotificationChannel();

    } catch (Throwable e) {
      e.printStackTrace();
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
      promise.reject(TAG, "pushService not initialized");
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
      promise.reject(TAG, "pushService not initialized");
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
      promise.reject(TAG, "pushService not initialized");
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
      promise.reject(TAG, "pushService not initialized");
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

  @ReactMethod
  public void checkPermission(Promise promise) {
    boolean state = NotificationManagerCompat.from(__rac).areNotificationsEnabled();
    promise.resolve(state);
  }

  @ReactMethod
  public void checkNetwork(Promise promise) {
    try {
      ConnectivityManager cm = (ConnectivityManager) __rac.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cm.getActiveNetworkInfo();
      Log.i(TAG, "checkNetwork" + info.isConnected() + " " + info.isAvailable() + " " + info.getType() + " " + info.getState());
      if (info != null) {
        promise.resolve(info.isConnected());
      } else {
        promise.resolve(false);
      }
    } catch (Exception e) {
      Log.e(TAG, "checkNetwork", e);
      promise.reject(e);
    }
  }

  @ReactMethod
  public void openSettingsForNotification(Promise promise) {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    String packageName = __rac.getPackageName();
    int uid = __rac.getApplicationInfo().uid;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra("app_package", packageName);
        intent.putExtra("app_uid", uid);
      }

      __rac.startActivity(intent);
    } catch (Exception e) {
      Log.e(TAG, "gotoNotificationSetting", e);

      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      intent.setData(Uri.fromParts("package", packageName, null));

      __rac.startActivity(intent);
    }
    promise.resolve(true);
  }
}
