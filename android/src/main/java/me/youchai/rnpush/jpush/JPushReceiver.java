package me.youchai.rnpush.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import cn.jpush.android.api.JPushInterface;
import me.youchai.rnpush.Notification;
import me.youchai.rnpush.RNPushModule;
import me.youchai.rnpush.utils.Logger;


/**
 * 接收自定义消息,通知,通知点击事件等事件的广播
 * 文档链接:http://docs.jiguang.cn/client/android_api/
 */
public class JPushReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent data) {
    Bundle bundle = data.getExtras();
    if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(data.getAction())) {
      // sent by sendMessage
      String id = String.valueOf(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0));
      String title = bundle.getString(JPushInterface.EXTRA_TITLE);
      String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
      String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
      Logger.i("receive message " + title + " " + content);

      RNPushModule.onNotification(new Notification(
          id, title, content, extras
      ));
    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(data.getAction())) {
      // sent by sendNotification
      String id = String.valueOf(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0));
      String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
      String content = bundle.getString(JPushInterface.EXTRA_ALERT);
      String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
      Logger.i("receive notification " + title + " " + content);

      RNPushModule.onNotification(new Notification(
          id, title, content, extras
      ));
    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(data.getAction())) {
      // 这里点击通知跳转到指定的界面可以定制化一下
      try {
        String id = String.valueOf(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0));
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d("notification click " + title);

        // start MainActivity
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        RNPushModule.onNotificationClick(new Notification(
            id, title, content, extras
        ));
      } catch (Exception e) {
        e.printStackTrace();
        Logger.i("error when click on notification");
      }
    } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(data.getAction())) {
      try {
        String registrationId = data.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
        Logger.d("注册成功, registrationId: " + registrationId);
        RNPushModule.onRegister("JPush", registrationId);
      } catch (Exception e) {
        e.printStackTrace();
        RNPushModule.onRegisterError(e.getMessage());
      }
    }
  }

}
