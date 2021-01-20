package me.champeau.gradle.japicmp.ignore;

import me.champeau.gradle.japicmp.ignore.entity.ClassDescriptor;
import me.champeau.gradle.japicmp.ignore.entity.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Project {
  public final String name;
  private final Map<String, ClassDescriptor> classes = new HashMap<>();

  public Project(String name) {
    this.name = name;
  }

  public void add(Entity entity) {
    ClassDescriptor classDescriptor = classes.computeIfAbsent(entity.getClassName(), ClassDescriptor::new);
    classDescriptor.add(entity);
  }

  public void addAll(Collection<Entity> entities) {
    for (Entity entity : entities) {
      add(entity);
    }
  }

  public Collection<ClassDescriptor> classes() {
    return classes.values();
  }
}
