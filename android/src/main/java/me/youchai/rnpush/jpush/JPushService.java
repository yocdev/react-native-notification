package me.youchai.rnpush.jpush;

import com.facebook.react.bridge.ReactApplicationContext;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import me.youchai.rnpush.Notification;
import me.youchai.rnpush.PushService;

public class JPushService extends PushService {
  private ReactApplicationContext _rac;

  public JPushService(ReactApplicationContext rac) {
    this._rac = rac;
  }

  public void init() {
    JPushInterface.init(_rac);
  }

  public void stop() {
    JPushInterface.stopPush(_rac);
  }

  public void resume() {
    JPushInterface.resumePush(_rac);
  }

  public String getName() {
    return "JPUSH_Android";
  }

  public String getRegistrationId() {
    return JPushInterface.getRegistrationID(_rac);
  }

  public void removeNotifications(List<String> ids) throws Exception {
    for (String id: ids) {
      JPushInterface.clearNotificationById(_rac, Integer.parseInt(id));
    }
  }

  public void removeAllNotifications() throws Exception {
    JPushInterface.clearAllNotifications(_rac);
  }

  public void scheduleLocalNotification(Notification notification) {
    JPushLocalNotification ln = new JPushLocalNotification();
    ln.setBuilderId(0);
    ln.setTitle(notification.getTitle());
    ln.setContent(notification.getContent());
    ln.setExtras(notification.getExtras());
    ln.setBroadcastTime(notification.getFireDate());

    JPushInterface.addLocalNotification(_rac, ln);
  }

  public void cancelLocalNotifications(List<String> ids) {
    for (String id: ids) {
      JPushInterface.removeLocalNotification(_rac, Long.parseLong(id));
    }
  }

  public void cancelAllLocalNotifications() {
    JPushInterface.clearLocalNotifications(_rac);
  }
}
