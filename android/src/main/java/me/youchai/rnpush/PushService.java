
package me.youchai.rnpush;

import android.content.Context;

public interface PushService {
  void init();
  void stop();
  void resume();

  String getRegistrationID();
  void clearAllNotification() throws Exception;
  void clearNotificationById(Context ctx, String id) throws Exception;
}
