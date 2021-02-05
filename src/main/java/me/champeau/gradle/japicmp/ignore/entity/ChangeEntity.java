package me.champeau.gradle.japicmp.ignore.entity;

import me.champeau.gradle.japicmp.ignore.element.Element;

public class ChangeEntity<T> extends Entity<T> {
  private final Element<T> oldElement;
  private final Element<T> newElement;

  public ChangeEntity(String version, Element<T> oldElement, Element<T> newElement) {
    super(version);
    this.oldElement = oldElement;
    this.newElement = newElement;
  }

  public Element<T> getOldElement() {
    return oldElement;
  }

  public Element<T> getNewElement() {
    return newElement;
  }

  @Override
  public String getIdentifier() {
    return oldElement.getIdentifier() + ":" + newElement.getIdentifier();
  }

  @Override
  public boolean process(Provider<T> provider) {
    Provider.Pair<T> changeElement = provider.getChangeElement();
    return this.oldElement.process(changeElement.getFirst()) && this.newElement.process(changeElement.getSecond());
  }
}
