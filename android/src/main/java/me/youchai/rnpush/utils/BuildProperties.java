/*
 * BuildProperties.java
 * youchai-android
 *
 * Created by kela.king on 2015-10-20
 * Copyright (c) 2015 youchai.me. All rights reserved.
 */
package me.youchai.rnpush.utils;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
/**
 * Created by kela.king on 15/10/20.
 */
public class BuildProperties {
  private final Properties _properties;
  private BuildProperties() throws IOException {
    _properties = new Properties();
    _properties
      .load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
  }
  public boolean containsKey(final Object key) {
    return _properties.containsKey(key);
  }
  public boolean containsValue(final Object value) {
    return _properties.containsValue(value);
  }
  public Set<Map.Entry<Object, Object>> entrySet() {
    return _properties.entrySet();
  }
  public String getProperty(final String name) {
    return _properties.getProperty(name);
  }
  public String getProperty(final String name, final String defaultValue) {
    return _properties.getProperty(name, defaultValue);
  }
  public boolean isEmpty() {
    return _properties.isEmpty();
  }
  public Enumeration<Object> keys() {
    return _properties.keys();
  }
  public Set<Object> keySet() {
    return _properties.keySet();
  }
  public int size() {
    return _properties.size();
  }
  public Collection<Object> values() {
    return _properties.values();
  }
  public static BuildProperties newInstance() throws IOException {
    return new BuildProperties();
  }
}
