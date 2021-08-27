package me.youchai.rnpush.jpush;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import me.youchai.rnpush.Notification;
import me.youchai.rnpush.RNPushModule;

public class JPushReceiver extends JPushMessageReceiver {
  private static final String TAG = "JPushReceiver";

  @Override
  public void onMessage(Context context, CustomMessage customMessage) {
    Log.i(TAG,"onMessage:"+customMessage);
    super.onMessage(context, customMessage);

  }

  @Override
  public void onNotifyMessageOpened(Context context, NotificationMessage msg) {
    Log.i(TAG,"onNotifyMessageOpened:"+msg);
    Intent intent = new Intent();
    intent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);

    String extras="";
    try {
      JSONObject jExtra = new JSONObject(msg.notificationExtras);
      extras = jExtra.getString("extras");
    }catch(Exception e){
      Log.e(TAG,"JSONObject",e);
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
    Log.i(TAG,"onMultiActionClicked");
    super.onMultiActionClicked(context, intent);
  }

  @Override
  public void onNotifyMessageArrived(Context context, NotificationMessage msg) {
    Log.i(TAG,"onNotifyMessageArrived:"+msg);
//    super.onNotifyMessageArrived(context, notificationMessage);
    String extras="";
    try {
      JSONObject jExtra = new JSONObject(msg.notificationExtras);
      extras = jExtra.getString("extras");
    }catch(Exception e){
      Log.e(TAG,"JSONObject",e);
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
    Log.i(TAG,"onNotifyMessageDismiss:"+notificationMessage);
    super.onNotifyMessageDismiss(context, notificationMessage);
  }

  @Override
  public void onRegister(Context context, String s) {
    Log.i(TAG,"onRegister:"+s);
    super.onRegister(context, s);
    RNPushModule.onRegister("JPUSH_Android", s);
  }

  @Override
  public void onCommandResult(Context context, CmdMessage cmdMessage) {
    Log.i(TAG,"onCommandResult:"+cmdMessage);
//    super.onCommandResult(context, cmdMessage);

    switch(cmdMessage.cmd) {
      case 2005:
        if(!cmdMessage.msg.isEmpty()){
          RNPushModule.onRegister("JPUSH_Android", cmdMessage.msg);
        }

        break;
    }
  }

  @Override
  public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG,"onTagOperatorResult:"+jPushMessage);
    super.onTagOperatorResult(context, jPushMessage);
  }

  @Override
  public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG,"onCheckTagOperatorResult"+jPushMessage);
    super.onCheckTagOperatorResult(context, jPushMessage);
  }

  @Override
  public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG,"onAliasOperatorResult:"+jPushMessage);
    super.onAliasOperatorResult(context, jPushMessage);
  }

  @Override
  public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
    Log.i(TAG,"onMobileNumberOperatorResult:"+jPushMessage);
    super.onMobileNumberOperatorResult(context, jPushMessage);
  }

  @Override
  public void onNotificationSettingsCheck(Context context, boolean b, int i) {
    Log.i(TAG,"onNotificationSettingsCheck:"+b+" i:"+i);
    super.onNotificationSettingsCheck(context, b, i);
  }


}
