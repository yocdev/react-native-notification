
package me.youchai.rnpush.jpush;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import me.youchai.rnpush.utils.Logger;
import me.youchai.rnpush.RNPushModule;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;


/**
 * 接收自定义消息,通知,通知点击事件等事件的广播
 * 文档链接:http://docs.jiguang.cn/client/android_api/
 */
public class JPushReceiver extends BroadcastReceiver {

  private static String TAG = "JPushReceiver";

  @Override
  public void onReceive(Context context, Intent data) {
    Bundle bundle = data.getExtras();

    if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(data.getAction())) {
      String message = data.getStringExtra(JPushInterface.EXTRA_MESSAGE);
      String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
      WritableMap map = Arguments.createMap();
      map.putString("message", message);
      map.putString("extras", extras);
      Logger.i(TAG, "收到自定义消息: " + message);
      RNPushModule.sendEvent("receivePushMsg", map);
    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(data.getAction())) {
      try {
        // 通知内容
        String alertContent = bundle.getString(JPushInterface.EXTRA_ALERT);
        // extra 字段的 json 字符串
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.i(TAG, "收到推送下来的通知: " + alertContent);
        //if (!isApplicationRunning(context)) {
          // HeadlessService 启动有问题，暂时弃用了
          //                        Log.i(TAG, "应用尚未切换到前台运行过，启动 HeadlessService");
          //                        Intent intent = new Intent(context, HeadlessService.class);
          //                        intent.putExtra("data", bundle);
          //                        context.startService(intent);
          //                        HeadlessJsTaskService.acquireWakeLockNow(context);
          // Save as local notification
          // Start up application failed, will save notifications as local notifications.
        //}
        WritableMap map = Arguments.createMap();
        map.putString("alertContent", alertContent);
        map.putString("extras", extras);
        RNPushModule.sendEvent("receiveNotification", map);
      } catch (Exception e) {
        e.printStackTrace();
      }

      // 这里点击通知跳转到指定的界面可以定制化一下
    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(data.getAction())) {
      try {
        String registrationId = data.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
        Logger.d(TAG, "用户点击打开了通知");

        // 通知内容
        String alertContent = bundle.getString(JPushInterface.EXTRA_ALERT);
        // extra 字段的 json 字符串
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        WritableMap map = Arguments.createMap();
        map.putString("alertContent", alertContent);
        map.putString("extras", extras);
        map.putString("jumpTo", "second");
        // judge if application is running in background, opening initial Activity.
        // You can change here to open appointed Activity. All you need to do is create
        // the appointed Activity, and use JS render the appointed Activity.
        // Please reference examples' SecondActivity for detail,
        // and JS files are in folder: example/react-native-android
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        // 如果需要跳转到指定的界面，那么需要同时启动 MainActivity 及指定界面：
        // If you need to open appointed Activity, you need to start MainActivity and
        // appointed Activity at the same time.
        //                    Intent detailIntent = new Intent();
        //                    detailIntent.setClassName(context.getPackageName(), context.getPackageName() + ".SecondActivity");
        //                    detailIntent.putExtras(bundle);
        //                    Intent[] intents = {intent, detailIntent};
        // 同时启动 MainActivity 以及 SecondActivity
        //                    context.startActivities(intents);
        RNPushModule.sendEvent("openNotification", map);
      } catch (Exception e) {
        e.printStackTrace();
        Logger.i(TAG, "Shouldn't access here");
      }
    // 应用注册完成后会发送广播，在 JS 中 RNPushModule.addGetRegistrationIdListener 接口可以第一时间得到 registrationId
    // After JPush finished registering, will send this broadcast, use RNPushModule.addGetRegistrationIdListener
    // to get registrationId in the first instance.
    } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(data.getAction())) {
      String registrationId = data.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
      Logger.d(TAG, "注册成功, registrationId: " + registrationId);
      try {
        WritableMap map = Arguments.createMap();
        map.putString("registrationId", registrationId);
        RNPushModule.sendEvent("getRegistrationID", map);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
