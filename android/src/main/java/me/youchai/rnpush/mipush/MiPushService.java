
package me.youchai.rnpush.mipush;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import me.youchai.rnpush.Notification;
import me.youchai.rnpush.PushService;
import me.youchai.rnpush.utils.Logger;

public class MiPushService extends PushService {
  private ReactApplicationContext _rac;

  public MiPushService(ReactApplicationContext rac) {
    this._rac = rac;
  }

  @Override
  public void init() {
    try {
      ApplicationInfo ai = _rac.getPackageManager()
          .getApplicationInfo(_rac.getPackageName(), PackageManager.GET_META_DATA);
      Bundle bundle = ai.metaData;
      String appid = bundle.getString("MIPUSH_APPID", null);
      String appkey = bundle.getString("MIPUSH_APPKEY", null);
      Logger.i(appid + " - " + appkey);
      MiPushClient.registerPush(_rac, appkey, appid);
    } catch (PackageManager.NameNotFoundException e) {
      Logger.i("cannot read metadata");
    }
  }

  @Override
  public void stop() {
    MiPushClient.disablePush(_rac);
  }

  @Override
  public void resume() {
    MiPushClient.enablePush(_rac);
  }

  @Override
  public String getName() {
    return "MiPush";
  }

  @Override
  public String getRegistrationId() {
    return MiPushClient.getRegId(_rac);
  }

  @Override
  public void removeNotifications(List<String> ids) throws Exception {
    for (String id : ids) {
      MiPushClient.clearNotification(_rac, Integer.parseInt(id));
    }
  }

  @Override
  public void removeAllNotifications() throws Exception {
    MiPushClient.clearNotification(_rac);
  }

  @Override
  public void scheduleLocalNotification(Notification notification) {
    Logger.i("not supported");
  }

  @Override
  public void cancelLocalNotifications(List<String> ids) {
    Logger.i("not supported");
  }

  @Override
  public void cancelAllLocalNotifications() {
    Logger.i("not supported");
  }
}
