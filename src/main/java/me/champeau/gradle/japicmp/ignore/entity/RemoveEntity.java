package me.champeau.gradle.japicmp.ignore.entity;

import me.champeau.gradle.japicmp.ignore.element.Element;

public class RemoveEntity<T> extends Entity<T> {
  private final Element<T> element;

  public RemoveEntity(String version, Element<T> element) {
    super(version);
    this.element = element;
  }

  @Override
  public String getIdentifier() {
    return element.getIdentifier();
  }

  @Override
  public boolean process(Provider<T> provider) {
    return element.process(provider.getRemoveElement());
  }
}
