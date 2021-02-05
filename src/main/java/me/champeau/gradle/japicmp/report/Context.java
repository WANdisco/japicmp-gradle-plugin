package me.champeau.gradle.japicmp.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Context implements ViolationCheckContextWithViolations {
  // violations per fully-qualified class name
  private final Map<String, List<Violation>> violations = new LinkedHashMap<>();
  private final Map<String, Object> userData = new LinkedHashMap<>();

  private String currentClass;

  public String getCurrentClass() {
    return currentClass;
  }

  public void setCurrentClass(String currentClass) {
    this.currentClass = currentClass;
  }

  public void maybeAddViolation(Violation v) {
    if (v == null) {
      return;
    }
    if (currentClass == null) {
      throw new IllegalStateException();
    }
    List<Violation> violations = this.violations.get(currentClass);
    if (violations == null) {
      violations = new ArrayList<>();
      this.violations.put(currentClass, violations);
    }
    violations.add(v);
  }

  @Override
  public String getClassName() {
    return currentClass;
  }

  @Override
  public Map<String, ?> getUserData() {
    return userData;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getUserData(final String key) {
    return (T) userData.get(key);
  }

  @Override
  public <T> void putUserData(final String key, final T value) {
    userData.put(key, value);
  }

  @Override
  public Map<String, List<Violation>> getViolations() {
    return violations;
  }
}
