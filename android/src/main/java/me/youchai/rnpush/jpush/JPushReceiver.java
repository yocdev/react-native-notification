
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
import android.net.Uri;

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

import org.json.JSONObject;

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
      // sent by sendMessage
      String title = bundle.getString(JPushInterface.EXTRA_TITLE);
      String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
      String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
      Logger.i("receive message " + title + " " + message);

      // event to js
      WritableMap map = Arguments.createMap();
      map.putString("title", title);
      map.putString("message", message);
      map.putString("extra", extra);
      RNPushModule.sendEvent("message", map);
    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(data.getAction())) {
      // sent by sendNotification
      String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
      String content = bundle.getString(JPushInterface.EXTRA_ALERT);
      String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
      Logger.i("receive notification " + title + " " + content);

      // event to js
      WritableMap map = Arguments.createMap();
      map.putString("content", content);
      map.putString("extra", extra);
      RNPushModule.sendEvent("notification", map);
    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(data.getAction())) {
      // 这里点击通知跳转到指定的界面可以定制化一下
      try {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d("notification click " + title);

        Intent intent = new Intent();
        JSONObject jExtra = new JSONObject(extra);
        if (jExtra.has("openUrl") && !jExtra.isNull("openUrl")) {
          String url = jExtra.getString("openUrl");
          Logger.i("openning url: " + url);
          intent.setAction(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(url));
          context.startActivity(intent);
        } else {
          intent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
          intent.putExtras(bundle);
          // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          context.startActivity(intent);
          WritableMap map = Arguments.createMap();
          map.putString("title", title);
          map.putString("content", content);
          map.putString("extra", extra);

          RNPushModule.sendEvent("openNotification", map);
        }
      } catch (Exception e) {
        e.printStackTrace();
        Logger.i("error when click on notification");
      }
    } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(data.getAction())) {
      String registrationId = data.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
      Logger.d("注册成功, registrationId: " + registrationId);
      try {
        WritableMap map = Arguments.createMap();
        map.putString("type", "JPush");
        map.putString("registrationId", registrationId);
        RNPushModule.sendEvent("register", map);
      } catch (Exception e) {
        e.printStackTrace();

        WritableMap map = Arguments.createMap();
        map.putString("error", e.getMessage());
        RNPushModule.sendEvent("registerError", map);
      }
    }
  }

}
