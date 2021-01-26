package me.champeau.gradle.japicmp.ignore.entity;

import me.champeau.gradle.japicmp.archive.Version;

public abstract class Entity {
  private final Version version;

  protected Entity(String version) {
    this.version = new Version(version);
  }

  public Version getVersion() {
    return version;
  }

  public abstract String getClassName();
}
