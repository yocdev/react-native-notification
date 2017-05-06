
package me.youchai.rnpush;

import me.youchai.rnpush.jpush.JPushService;
import me.youchai.rnpush.mipush.MiPushService;

import com.facebook.react.bridge.ReactApplicationContext;

public class PushServiceFactory {
  public static PushService create(ReactApplicationContext rac) {
    MiPushService miservice = new MiPushService(rac);
    miservice.stop();
    JPushService jservice = new JPushService(rac);
    jservice.resume();
    return jservice;
  }
}
