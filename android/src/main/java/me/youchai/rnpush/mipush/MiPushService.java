
package me.youchai.rnpush.mipush;

import android.content.Context;
import com.facebook.react.bridge.ReactApplicationContext;

import com.xiaomi.mipush.sdk.MiPushClient;

import me.youchai.rnpush.PushService;

public class MiPushService implements PushService {
  private ReactApplicationContext _rac;

  public MiPushService(ReactApplicationContext rac) {
    this._rac = rac;
  }

  @Override
  public void init() {
    MiPushClient.registerPush(_rac, "", "");
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
  public String getRegistrationID() {
    return MiPushClient.getRegId(_rac);
  }

  @Override
  public void clearAllNotification() throws Exception {
    MiPushClient.clearNotification(_rac);
  }

  @Override
  public void clearNotificationById(String id) throws Exception {
    MiPushClient.clearNotification(_rac, Integer.parseInt(id));
  }
}
