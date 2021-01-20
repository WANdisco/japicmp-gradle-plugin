package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;

public class RemoveMethodEntity extends Entity {
  private final MethodElement entity;

  public RemoveMethodEntity(String version, MethodElement entity) {
    super(version);
    this.entity = entity;
  }

  public boolean validate(JApiMethod method) {
    return entity.process(method);
  }

  public String getName() {
    return entity.getName();
  }

  @Override
  public String getClassName() {
    return entity.getCanonicalName();
  }
}
