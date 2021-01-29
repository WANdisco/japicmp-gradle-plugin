package me.champeau.gradle.japicmp.ignore;

import me.champeau.gradle.japicmp.ignore.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeclaredModule {
  public final String name;
  private final List<Entity<?>> entities = new ArrayList<>();

  public DeclaredModule(String name) {
    this.name = name;
  }

  public void add(Entity<?> entity) {
    entities.add(entity);
  }

  public List<Entity<?>> getEntities() {
    return Collections.unmodifiableList(entities);
  }
}
