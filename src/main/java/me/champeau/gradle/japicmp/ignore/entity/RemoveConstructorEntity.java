package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiConstructor;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;

public class RemoveConstructorEntity extends Entity {
  private final ConstructorElement entity;

  public RemoveConstructorEntity(String version, ConstructorElement entity) {
    super(version);
    this.entity = entity;
  }

  public boolean validate(JApiConstructor constructor) {
    return entity.process(constructor);
  }

  public String getName() {
    return entity.getName();
  }

  @Override
  public String getClassName() {
    return entity.getCanonicalName();
  }
}
