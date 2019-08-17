package me.youchai.rnpush.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.graphics.Color;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import java.lang.System;

import android.R;

import android.support.v4.app.NotificationCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import cn.jpush.android.api.JPushInterface;
import me.youchai.rnpush.RNPushModule;
import me.youchai.rnpush.utils.Logger;

import org.json.JSONObject;
import android.net.Uri;


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

      RNPushModule.onNotification(new me.youchai.rnpush.Notification(
          id, title, content, extras
      ));
    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(data.getAction())) {
      // sent by sendNotification
      String id = String.valueOf(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0));
      String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
      String content = bundle.getString(JPushInterface.EXTRA_ALERT);
      String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

      if (Build.VERSION.SDK_INT >= 26) {
        Logger.i("android 8.0 以上推送广播处理");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID); // 定义通知id
        // 创建 channel
        String channelId = context.getPackageName();// 通知渠道id
        String channelName = "viewers"; // "PUSH_NOTIFY_NAME"; //通知渠道名
        int importance = NotificationManager.IMPORTANCE_HIGH; // 通知级别
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setLightColor(Color.RED);
        channel.setShowBadge(true);
        channel.setVibrationPattern(new long[] { 100, 200, 300, 400, 500, 400, 300, 200, 400 });
        notificationManager.createNotificationChannel(channel);

        // 通知点击操作的参数
        Bundle intentParams = new Bundle();
        intentParams.putInt(JPushInterface.EXTRA_NOTIFICATION_ID, bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0));
        intentParams.putString(JPushInterface.EXTRA_TITLE, title);
        intentParams.putString(JPushInterface.EXTRA_MESSAGE, content);
        intentParams.putString(JPushInterface.EXTRA_EXTRA, extras);
        // 用于响应通知点击操作的 intent
        Intent intent0 = new Intent(context, JPushReceiver.class);
        intent0.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent0.putExtras(intentParams);
        PendingIntent pi = PendingIntent.getBroadcast(context, notificationId, intent0, PendingIntent.FLAG_UPDATE_CURRENT);

        // 获取状态栏显示的通知图标
        int drawableId = context.getResources().getIdentifier("ic_notification", "drawable", context.getPackageName());
        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setContentTitle(title) // 设置通知栏标题
            .setContentText(content)
            .setWhen(System.currentTimeMillis()) // 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
            .setSmallIcon(drawableId)
            .setChannelId(channelId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pi);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (notificationManager != null) {
          notificationManager.notify(notificationId, notification);
        }
      } else {
        RNPushModule.onNotification(new me.youchai.rnpush.Notification(
          id, title, content, extras
        ));
      }
    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(data.getAction())) {
      // 这里点击通知跳转到指定的界面可以定制化一下
      try {
        String id = String.valueOf(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0));
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        Intent intent = new Intent();
        JSONObject jExtra = new JSONObject(extras);
        if (jExtra.has("openUrl") && !jExtra.isNull("openUrl")) {
          String url = jExtra.getString("openUrl");
          Logger.i("openning url: " + url);
          intent.setAction(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(url));
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          context.startActivity(intent);
        } else {
          intent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          context.startActivity(intent);
        }

        RNPushModule.onNotificationClick(new me.youchai.rnpush.Notification(
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
