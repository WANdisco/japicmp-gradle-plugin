package me.champeau.gradle.japicmp.ignore.entity;

public interface Provider<T> {
  T getRemoveElement();

  Pair<T> getChangeElement();

  String getIdentifier();

  class Pair<T> {
    private final T first;
    private final T second;

    public Pair(T first, T second) {
      this.first = first;
      this.second = second;
    }

    public T getFirst() {
      return first;
    }

    public T getSecond() {
      return second;
    }
  }

  abstract class OnlyRemoveElementProvider<T> implements Provider<T> {
    private final T removeElement;

    public OnlyRemoveElementProvider(T removeElement) {
      this.removeElement = removeElement;
    }

    @Override
    public T getRemoveElement() {
      return removeElement;
    }

    @Override
    public Pair<T> getChangeElement() {
      throw new UnsupportedOperationException("Only remove element provider");
    }
  }

  abstract class OnlyChangeElementProvider<T> implements Provider<T> {
    private final Pair<T> changeElement;

    public OnlyChangeElementProvider(T fromChangeElement, T toChangeElement) {
      this.changeElement = new Pair<>(fromChangeElement, toChangeElement);
    }

    @Override
    public T getRemoveElement() {
      throw new UnsupportedOperationException("Only change element provider");
    }

    @Override
    public Pair<T> getChangeElement() {
      return changeElement;
    }
  }
}
