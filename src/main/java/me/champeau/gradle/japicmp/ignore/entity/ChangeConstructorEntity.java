package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiConstructor;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;


public class ChangeConstructorEntity extends Entity {
  private final ConstructorElement prevValue;
  private final ConstructorElement newValue;

  public ChangeConstructorEntity(String version, ConstructorElement prevValue, ConstructorElement newValue) {
    super(version);
    this.prevValue = prevValue;
    this.newValue = newValue;
  }

  public boolean validate(JApiConstructor oldConstructor, JApiConstructor newConstructor) {
    if (prevValue.getClass() != newValue.getClass()) return false;

    boolean prevValidate = prevValue.process(oldConstructor);
    boolean newValidate = newValue.process(newConstructor);

    return prevValidate && newValidate;
  }

  public String getName() {
    return prevValue.getName();
  }

  @Override
  public String getClassName() {
    return prevValue.getCanonicalName();
  }
}
