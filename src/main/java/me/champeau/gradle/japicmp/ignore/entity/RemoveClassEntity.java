package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiClass;
import me.champeau.gradle.japicmp.ignore.element.ClassElement;

public class RemoveClassEntity extends Entity {
  private final ClassElement element;

  public RemoveClassEntity(String version, ClassElement classElement) {
    super(version);
    this.element = classElement;
  }

  @Override
  public String getClassName() {
    return element.getCanonicalName();
  }

  public boolean validate(JApiClass jApiClass) {
    return element.process(jApiClass);
  }
}
