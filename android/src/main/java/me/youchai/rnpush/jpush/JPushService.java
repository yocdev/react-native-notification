
package me.youchai.rnpush.jpush;

import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;

import cn.jpush.android.api.JPushInterface;

import me.youchai.rnpush.PushService;

public class JPushService implements PushService {
  private ReactApplicationContext _rac;

  public JPushService(ReactApplicationContext rac) {
    this._rac = rac;
  }

  @Override
  public void init() {
    JPushInterface.init(_rac);
  }

  @Override
  public void stop() {
    JPushInterface.stopPush(_rac);
  }

  @Override
  public void resume() {
    JPushInterface.resumePush(_rac);
  }

  @Override
  public String getName() {
    return "JPush";
  }

  @Override
  public String getRegistrationId() {
    return JPushInterface.getRegistrationID(_rac);
  }

  @Override
  public void clearAllNotification() throws Exception {
    JPushInterface.clearAllNotifications(_rac);
  }

  @Override
  public void clearNotificationById(String id) throws Exception {
    JPushInterface.clearNotificationById(_rac, Integer.parseInt(id));
  }
}
