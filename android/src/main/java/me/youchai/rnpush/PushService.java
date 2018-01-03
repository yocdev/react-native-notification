package me.youchai.rnpush;

import java.util.List;

public abstract class PushService {
  private static Notification initialNotification;


  public abstract void init();
  public abstract void stop();
  public abstract void resume();

  public abstract String getName();
  public abstract String getRegistrationId();

  public abstract void removeNotifications(List<String> ids) throws Exception;
  public abstract void removeAllNotifications() throws Exception;

  public abstract void scheduleLocalNotification(Notification notification);
  public abstract void cancelLocalNotifications(List<String> ids);
  public abstract void cancelAllLocalNotifications();

  static void setInitialNotification(Notification notification) {
    initialNotification = notification;
  }
  static Notification getInitialNotification() {
    return initialNotification;
  }
}
