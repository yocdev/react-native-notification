package me.youchai.rnpush;

import android.os.Build;
import java.io.IOException;

public class RomUtils {
  private static final String __KEY_MIUI_VERSION_CODE     = "ro.miui.ui.version.code";
  private static final String __KEY_MIUI_VERSION_NAME     = "ro.miui.ui.version.name";
  private static final String __KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
  private static final String __MIUI_V6_VERSION_CODE      = "V6";
  private static final String __KEY_EMUI_VERSION_NAME        = "ro.build.version.emui";
  private static final String __KEY_EMUI_SYSTEM_VERSION_NAME = "ro.confg.hw_systemversion";

  // TODO 没有很准确的方法获取是否什么系统
  public static boolean isMiui() {
    try {
      final BuildProperties prop = BuildProperties.newInstance();
      return prop.getProperty(__KEY_MIUI_VERSION_CODE, null) != null
        || prop.getProperty(__KEY_MIUI_VERSION_NAME, null) != null
        || prop.getProperty(__KEY_MIUI_INTERNAL_STORAGE, null) != null;
    } catch (IOException e) {
      return false;
    }
  }
  public static boolean isMiui6() {
    try {
      final BuildProperties prop = BuildProperties.newInstance();
      return __MIUI_V6_VERSION_CODE.equals(prop.getProperty(__KEY_MIUI_VERSION_NAME, null));
    } catch (IOException e) {
      return false;
    }
  }

  public static boolean isEmui() {
    try {
      final BuildProperties prop = BuildProperties.newInstance();
      return prop.getProperty(__KEY_EMUI_VERSION_NAME, null) != null
        || prop.getProperty(__KEY_EMUI_SYSTEM_VERSION_NAME, null) != null;
    } catch (IOException e) {
      return false;
    }
  }
  public static boolean isCustomRom() {
    return android.os.Build.HOST.toLowerCase().contains("google.com");
  }
}
