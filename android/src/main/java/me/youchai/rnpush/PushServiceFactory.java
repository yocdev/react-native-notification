
package me.youchai.rnpush;

import me.youchai.rnpush.jpush.JPushService;
import com.facebook.react.bridge.ReactApplicationContext;

public class PushServiceFactory {
  public static PushService create(ReactApplicationContext rac) {
    return new JPushService(rac);
  }
}
