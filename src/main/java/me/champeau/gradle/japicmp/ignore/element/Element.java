package me.champeau.gradle.japicmp.ignore.element;


public abstract class Element<T> {
  private final String fullPackageName;
  private final String className;

  public Element(String fullPackageName, String className) {
    this.fullPackageName = fullPackageName;
    this.className = className;
  }

  public String getPackage() {
    return fullPackageName;
  }

  public String getClassName() {
    return className;
  }

  public String getCanonicalName() {
    return fullPackageName + "." + className;
  }

  public final String getIdentifier() {
    String s = doGetIdentifier();
    return getCanonicalName() + ((s != null && !s.isEmpty()) ? ":" + s : "");
  }

  protected abstract String doGetIdentifier();

  public abstract boolean process(T target);
}
