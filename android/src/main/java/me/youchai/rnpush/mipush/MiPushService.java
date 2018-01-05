
package me.youchai.rnpush.mipush;

import android.content.Context;
import com.facebook.react.bridge.ReactApplicationContext;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import me.youchai.rnpush.Notification;
import me.youchai.rnpush.PushService;
import me.youchai.rnpush.utils.Logger;
import me.youchai.rnpush.R;

public class MiPushService extends PushService {
  private ReactApplicationContext _rac;

  public MiPushService(ReactApplicationContext rac) {
    this._rac = rac;
  }

  @Override
  public void init() {
    String appkey = _rac.getString(R.string.mipush_appkey);
    String appid = _rac.getString(R.string.mipush_appid);
    MiPushClient.registerPush(_rac, appkey, appid);
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
