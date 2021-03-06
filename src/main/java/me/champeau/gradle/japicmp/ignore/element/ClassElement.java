package me.champeau.gradle.japicmp.ignore.element;

import japicmp.model.JApiClass;

import java.util.Objects;

public class ClassElement extends Element<JApiClass> {
  public ClassElement(String fullPackageName, String className) {
    super(fullPackageName, className);
  }

  @Override
  protected String doGetIdentifier() {
    return "";
  }

  @Override
  public boolean process(JApiClass target) {
    return Objects.equals(target.getFullyQualifiedName(), getCanonicalName());
  }

  public static String extractIdentifier(JApiClass jApiClass) {
    return jApiClass.getFullyQualifiedName();
  }
}
