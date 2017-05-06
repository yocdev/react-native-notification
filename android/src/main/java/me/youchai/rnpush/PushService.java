
package me.youchai.rnpush;

import android.content.Context;

public interface PushService {
  void init();
  void stop();
  void resume();

  String getRegistrationId();
  void clearAllNotification() throws Exception;
  void clearNotificationById(String id) throws Exception;
}
