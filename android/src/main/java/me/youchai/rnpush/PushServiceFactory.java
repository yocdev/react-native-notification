
package me.youchai.rnpush;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.youchai.rnpush.jpush.JPushService;
import me.youchai.rnpush.utils.Logger;
import me.youchai.rnpush.utils.RomUtils;

class PushServiceFactory {
  private static final String JPUSH = "JPUSH_Android";

  private static final Map<String, Class<? extends PushService>> allServices =
      Collections.unmodifiableMap(new HashMap() {{
        put(JPUSH, JPushService.class);
      }});

  private static String getSystemType() {
    return JPUSH;
  }

  static PushService create(ReactApplicationContext rac, ReadableMap config) throws Exception {
    String systemType = getSystemType();
    if (config != null && config.hasKey("type")) {
      systemType = config.getString("type");
    }

    Logger.i("current type is " + systemType);
    Class<? extends PushService> serviceClass = allServices.get(systemType);
    Constructor<? extends PushService> c =
        serviceClass.getConstructor(ReactApplicationContext.class);
    return c.newInstance(rac);
  }
}
