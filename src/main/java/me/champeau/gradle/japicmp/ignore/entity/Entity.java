package me.champeau.gradle.japicmp.ignore.entity;

//TODO: Implement version supporting
public abstract class Entity {
  private final String version;

  protected Entity(String version) {
    this.version = version;
  }

  public abstract String getClassName();
}
