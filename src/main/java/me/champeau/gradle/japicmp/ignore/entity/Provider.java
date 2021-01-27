package me.champeau.gradle.japicmp.ignore.entity;

public interface Provider<T> {
  T getRemoveElement();

  Pair<T> getChangeElement();

  String getIdentifier();

  class Pair<T> {
    private T first;
    private T second;

    public Pair(T first, T second) {
      this.first = first;
      this.second = second;
    }

    public void setFirst(T first) {
      this.first = first;
    }

    public void setSecond(T second) {
      this.second = second;
    }

    public T getFirst() {
      return first;
    }

    public T getSecond() {
      return second;
    }
  }

  interface MutableProvider<T> extends Provider<T> {
    void setRemoveElement(T element);

    void setChangeElement(T oldElement, T newElement);
  }


  abstract class OnlyRemoveElementProvider<T> implements MutableProvider<T> {
    private T removeElement;

    @Override
    public T getRemoveElement() {
      return removeElement;
    }

    @Override
    public void setRemoveElement(T method) {
      this.removeElement = method;
    }

    @Override
    public void setChangeElement(T oldMethod, T newMethod) {
      throw new UnsupportedOperationException("Only remove element provider");
    }

    @Override
    public Pair<T> getChangeElement() {
      throw new UnsupportedOperationException("Only remove element provider");
    }
  }

  public abstract class MutableProviderImpl<T> implements MutableProvider<T> {
    private T removeElement;
    private Pair<T> changeElement;

    public void setRemoveElement(T method) {
      removeElement = method;
    }

    public void setChangeElement(T oldMethod, T newMethod) {
      changeElement = new Pair<>(oldMethod, newMethod);
    }

    @Override
    public T getRemoveElement() {
      return removeElement;
    }

    @Override
    public Pair<T> getChangeElement() {
      return changeElement;
    }
  }


}
