
package me.youchai.rnpush.mipush;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.ErrorCode;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONObject;

import me.youchai.rnpush.Notification;
import me.youchai.rnpush.RNPushModule;
import me.youchai.rnpush.utils.Logger;

public class MiPushReceiver extends PushMessageReceiver {

  @Override
  public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
    String id = String.valueOf(message.getNotifyId());
    String title = message.getTitle();
    String content = message.getDescription();
    Map<String, String> extrasMap = message.getExtra();
    String extras = new JSONObject(extrasMap).toString();

    RNPushModule.onNotification(new Notification(
        id, title, content, extras
    ));
  }
  @Override
  public void onNotificationMessageClicked(Context context, MiPushMessage message) {
    String id = String.valueOf(message.getNotifyId());
    String title = message.getTitle();
    String content = message.getDescription();
    Map<String, String> extrasMap = message.getExtra();
    String extras = new JSONObject(extrasMap).toString();
    Logger.i(message.toString());

    // start MainActivity
    Intent intent = new Intent();
    intent.setClassName(context.getPackageName(), context.getPackageName() + ".MainActivity");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);

    RNPushModule.onNotificationClick(new Notification(
        id, title, content, extras
    ));
  }
  @Override
  public void onNotificationMessageArrived(Context context, MiPushMessage message) {
    String id = String.valueOf(message.getNotifyId());
    String title = message.getTitle();
    String content = message.getDescription();
    Map<String, String> extrasMap = message.getExtra();
    String extras = new JSONObject(extrasMap).toString();

    RNPushModule.onNotification(new Notification(
        id, title, content, extras
    ));
  }

  @Override
  public void onCommandResult(Context context, MiPushCommandMessage message) {
    String command = message.getCommand();
    List<String> arguments = message.getCommandArguments();
    String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
    if (MiPushClient.COMMAND_REGISTER.equals(command)) {
      if (message.getResultCode() == ErrorCode.SUCCESS) {
        Logger.d("注册成功, registrationId: " + cmdArg1);
        RNPushModule.onRegister("MiPush", cmdArg1);
      }
    }
  }

  @Override
  public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
    String command = message.getCommand();
    List<String> arguments = message.getCommandArguments();
    String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);

    Logger.i(message.toString());
    if (MiPushClient.COMMAND_REGISTER.equals(command)) {
      if (message.getResultCode() == ErrorCode.SUCCESS) {
        Logger.d("注册成功, registrationId: " + cmdArg1);
        RNPushModule.onRegister("MiPush", cmdArg1);
      }
    }
  }
}
