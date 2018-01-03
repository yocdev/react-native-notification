package me.youchai.rnpush;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

public class Notification {
  private String id = "";
  private String title = "";
  private String content = "";
  private String extras = "{}";
  private long fireDate = 0L;

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getExtras() {
    return extras;
  }

  public String getId() {
    return id;
  }

  public long getFireDate() {
    return fireDate;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public void setFireDate(long milliseconds) {
    this.fireDate = milliseconds;
  }

  public Notification() {
  }

  public Notification(String id, String title, String content, String extras) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.extras = extras;
  }

  public WritableMap toWritableMap() {
    WritableMap map = Arguments.createMap();
    map.putString("title", title);
    map.putString("content", content);
    map.putString("extras", extras);

    return map;
  }
}
