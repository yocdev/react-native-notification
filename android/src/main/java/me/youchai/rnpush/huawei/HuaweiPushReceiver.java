package me.youchai.rnpush.huawei;

import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;

import me.youchai.rnpush.RNPushModule;
import me.youchai.rnpush.utils.Logger;

public class HuaweiPushReceiver extends PushReceiver {

  @Override
  public void onToken(Context context, String token) {
    RNPushModule.onRegister("Huawei", token);
  }

  @Override
  public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {
    String content = new String(msgBytes);
    Logger.i(content);
    Logger.i(extras.toString());
    return true;
  }

  @Override
  public void onEvent(Context context, Event event, Bundle extras) {
    Logger.i(event.toString());
    Logger.i(extras.toString());
  }
}
