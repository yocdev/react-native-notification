package me.youchai.rnpush;

import android.app.Activity;
import android.content.Context;

import cn.jpush.android.api.JPluginPlatformInterface;

public class RNPushPlatformInterface {
  private JPluginPlatformInterface platformInterface;

  public RNPushPlatformInterface(Context context){
    this.platformInterface = new JPluginPlatformInterface(context);
  }

  public void onStart(Activity activity){
    this.platformInterface.onStart(activity);
  }

  public void onStop(Activity activity){
    this.platformInterface.onStop(activity);
  }

}
