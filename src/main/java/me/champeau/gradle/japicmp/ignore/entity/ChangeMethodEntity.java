package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;


public class ChangeMethodEntity extends Entity {
  private final MethodElement prevValue;
  private final MethodElement newValue;

  public ChangeMethodEntity(String version, MethodElement prevValue, MethodElement newValue) {
    super(version);
    this.prevValue = prevValue;
    this.newValue = newValue;
  }

  public boolean validate(JApiMethod oldMethod, JApiMethod newMethod) {
    if (prevValue.getClass() != newValue.getClass()) return false;

    boolean prevValidate = prevValue.process(oldMethod);
    boolean newValidate = newValue.process(newMethod);

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
