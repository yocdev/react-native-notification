
package me.youchai.rnpush;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.lang.reflect.Constructor;

import me.youchai.rnpush.jpush.JPushService;
import me.youchai.rnpush.mipush.MiPushService;
import me.youchai.rnpush.utils.Logger;
import me.youchai.rnpush.utils.RomUtils;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;

class PushServiceFactory {
  private static final String XIAOMI = "xiaomi";
  private static final String HUAWEI = "huawei";
  private static final String OTHER = "default";

  private static final Map<String, Class<? extends PushService>> allServices =
    Collections.unmodifiableMap(new HashMap() {{
      put(XIAOMI, MiPushService.class);
      put(OTHER, JPushService.class);
    }});

  private static String getSystemType() {
    if (RomUtils.isEmui()) {
      // TODO add huawei
      return OTHER;
    } else if (RomUtils.isMiui()) {
      return XIAOMI;
    }
    return OTHER;
  }

  private static boolean isEnabled(ReadableArray enabled, String type) {
    for (int i = 0; i < enabled.size(); i++) {
      Logger.i(enabled.getString(i));
      if (type == enabled.getString(i)) {
        return true;
      }
    }
    return false;
  }

  static PushService create(ReactApplicationContext rac, ReadableMap config) {
    PushService wantedService = null;

    String systemType = OTHER;
    // check if systemType is enabled
    if (config != null && config.hasKey("enabled")) {
      systemType = getSystemType();
      if (!isEnabled(config.getArray("enabled"), systemType)) {
        systemType = OTHER;
      }
    }

    Logger.i("current type is " + systemType);
    for (Map.Entry<String, Class<? extends PushService>> entry : allServices.entrySet()) {
      try {
        Constructor<? extends PushService> c = entry.getValue().getConstructor(ReactApplicationContext.class);
        PushService service = c.newInstance(rac);
        if (entry.getKey() == systemType) {
          service.resume();
          wantedService = service;
        } else {
          service.stop();
        }
      } catch (Exception e) {
        // not likely
      }
    }

    return wantedService;
  }
}
