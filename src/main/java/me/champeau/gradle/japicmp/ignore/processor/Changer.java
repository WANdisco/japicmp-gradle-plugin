package me.champeau.gradle.japicmp.ignore.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Changer<T, R> {
  private final Function<T, String> nameExtractor;
  private final Function<T, String> matcher;

  private final Map<T, R> reasons = new HashMap<>();
  private final Map<String, T> changes = new HashMap<>();
  private final Map<T, T> matches = new HashMap<>();

  public Changer(Function<T, String> nameExtractor) {
    this(nameExtractor, nameExtractor);
  }

  public Changer(Function<T, String> nameExtractor, Function<T, String> matcher) {
    this.nameExtractor = nameExtractor;
    this.matcher = matcher;
  }

  public void addToChanges(T element, R reason) {
    reasons.put(element, reason);

    T match = changes.remove(matcher.apply(element));
    if (match != null) {
      matches.put(match, element);
    } else {
      changes.put(nameExtractor.apply(element), element);
    }
  }

  public Map<T, T> getMatches() {
    return Collections.unmodifiableMap(matches);
  }

  public List<T> getUnmatchedChanges() {
    return Collections.unmodifiableList(new ArrayList<>(changes.values()));
  }

  public R getReason(T element) {
    return reasons.get(element);
  }
}
