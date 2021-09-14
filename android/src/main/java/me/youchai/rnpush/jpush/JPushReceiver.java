package me.youchai.rnpush.jpush;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import me.youchai.rnpush.Notification;
import me.youchai.rnpush.RNPushModule;

public class JPushReceiver extends JPushMessageReceiver {
  private static final String TAG = "JPushReceiver";

  /**
   * 消息Id
   **/
  private static final String KEY_MSGID = "msg_id";
  /**
   * 该通知的下发通道
   **/
  private static final String KEY_WHICH_PUSH_SDK = "rom_type";
  /**
   * 通知标题
   **/
  private static final String KEY_TITLE = "n_title";
  /**
   * 通知内容
   **/
  private static final String KEY_CONTENT = "n_content";
  /**
   * 通知附加字段
   **/
  private static final String KEY_EXTRAS = "n_extras";

  private static Notification firmNotification = null;

  public static void openFirmNotification(Activity activity,Intent intent) {
    Log.i(TAG, "openFirmNotification");
    try {
//      Intent intent = activity.getIntent();

      String data = null;
      //获取华为平台附带的jpush信息
      if (intent.getData() != null) {
        data = intent.getData().toString();
      }

      //获取fcm、oppo、vivo、华硕、小米平台附带的jpush信息
      if (data == null && intent.getExtras() != null) {
        data = intent.getExtras().getString("JMessageExtra");
      }
      JSONObject json = new JSONObject(data);

      String extras = json.getJSONObject(KEY_EXTRAS).getString("extras");
      Log.d(TAG, "extras:" + extras);

      String msgId = json.optString(KEY_MSGID);
      byte whichPushSDK = (byte) json.optInt(KEY_WHICH_PUSH_SDK);
      String title = json.optString(KEY_TITLE);
      String content = json.optString(KEY_CONTENT);
      firmNotification = new Notification(
        msgId,
        title,
        content,
        extras
      );
      RNPushModule.onNotificationClick(firmNotification);
      JPushInterface.reportNotificationOpened(activity, msgId, whichPushSDK);
    } catch (Exception e) {
      Log.e(TAG, "getIntent()", e);
    }
  }

  @Override
  public void onMessage(Context context, CustomMessage customMessage) {
    Log.i(TAG, "onMessage:" + customMessage);
    super.onMessage(context, customMessage);
  }

  @Override
  public void onNotifyMessageOpened(Context context, NotificationMessage msg) {
    Log.i(TAG, "onNotifyMessageOpened:" + msg);
    Intent intent = new Intent();
    intent.setClassName(context.getPackageName(), "me.youchai.bmb.MainActivity");
    intent.setFlags(
      Intent.FLAG_ACTIVITY_NEW_TASK |
        Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);

    String extras = "";
    try {
      JSONObject jExtra = new JSONObject(msg.notificationExtras);
      extras = jExtra.getString("extras");
    } catch (Exception e) {
      Log.e(TAG, "JSONObject", e);
    }
//    super.onNotifyMessageOpened(context, notificationMessage);
    RNPushModule.onNotificationClick(new Notification(
      msg.msgId,
      msg.notificationTitle,
      msg.notificationContent,
      extras
    ));
  }

  @Override
  public void onMultiActionClicked(Context context, Intent intent) {
    Log.i(TAG, "onMultiActionClicked");
    super.onMultiActionClicked(context, intent);
  }

  @Override
  public void onNotifyMessageArrived(Context context, NotificationMessage msg) {
    Log.i(TAG, "onNotifyMessageArrived:" + msg);
//    super.onNotifyMessageArrived(context, notificationMessage);
    String extras = "";
    try {
      JSONObject jExtra = new JSONObject(msg.notificationExtras);
      extras = jExtra.getString("extras");
    } catch (Exception e) {
      Log.e(TAG, "JSONObject", e);
    }
    RNPushModule.onNotification(new me.youchai.rnpush.Notification(
      msg.msgId,
      msg.notificationTitle,
      msg.notificationContent,
      extras
    ));
  }

  @Override
  public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
    Log.i(TAG, "onNotifyMessageDismiss:" + notificationMessage);
    super.onNotifyMessageDismiss(context, notificationMessage);
  }

  @Override
  public void onRegister(Context context, String s) {
    Log.i(TAG, "onRegister:" + s);
    super.onRegister(context, s);
    RNPushModule.onRegister("JPUSH_Android", s);
  }

  void processFirmMessage(CmdMessage cmdMsg) {
    if (cmdMsg.extra == null) return;
    String token = cmdMsg.extra.getString("token");
    int platform = cmdMsg.extra.getInt("platform");
    String deviceName = "unkown";
    switch (platform) {
      case 1:
        deviceName = "小米";
        break;
      case 2:
        deviceName = "华为";
        break;
      case 3:
        deviceName = "魅族";
        break;
      case 4:
        deviceName = "OPPO";
        break;
      case 5:
        deviceName = "VIVO";
        break;
      case 6:
        deviceName = "ASUS";
        break;
      case 8:
        deviceName = "FCM";
        break;
    }
    Log.d(TAG, "获取到 " + deviceName + " 的token:" + token);
  }

  @Override
  public void onCommandResult(Context context, CmdMessage cmdMessage) {
    Log.i(TAG, "onCommandResult:" + cmdMessage);
//    super.onCommandResult(context, cmdMessage);
    if (cmdMessage == null) return;
    switch (cmdMessage.cmd) {
      case 10000:
        processFirmMessage(cmdMessage);
        break;

      case 2005:
        if (!cmdMessage.msg.isEmpty()) {
          RNPushModule.onRegister("JPUSH_Android", cmdMessage.msg);
        }
        if (firmNotification != null) {
          RNPushModule.onNotificationClick(firmNotification);
          firmNotification = null;
        }
        break;
    }
  }

  @Override
  public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG, "onTagOperatorResult:" + jPushMessage);
    super.onTagOperatorResult(context, jPushMessage);
  }

  @Override
  public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG, "onCheckTagOperatorResult" + jPushMessage);
    super.onCheckTagOperatorResult(context, jPushMessage);
  }

  @Override
  public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG, "onAliasOperatorResult:" + jPushMessage);
    super.onAliasOperatorResult(context, jPushMessage);
  }

  @Override
  public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG, "onMobileNumberOperatorResult:" + jPushMessage);
    super.onMobileNumberOperatorResult(context, jPushMessage);
  }

  @Override
  public void onNotificationSettingsCheck(Context context, boolean b, int i) {
    Log.i(TAG, "onNotificationSettingsCheck:" + b + " i:" + i);
    super.onNotificationSettingsCheck(context, b, i);
  }
}
