
package me.youchai.rnpush.mipush;

import android.content.Context;
import com.facebook.react.bridge.ReactApplicationContext;

import com.xiaomi.mipush.sdk.MiPushClient;

import me.youchai.rnpush.PushService;
import me.youchai.rnpush.utils.Logger;
import me.youchai.rnpush.R;

public class MiPushService implements PushService {
  private ReactApplicationContext _rac;

  public MiPushService(ReactApplicationContext rac) {
    this._rac = rac;
  }

  @Override
  public void init() {
    String appkey = _rac.getString(R.string.mipush_appkey);
    String appid = _rac.getString(R.string.mipush_appid);
    MiPushClient.registerPush(_rac, appid, appkey);
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
  public String getRegistrationId() {
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
