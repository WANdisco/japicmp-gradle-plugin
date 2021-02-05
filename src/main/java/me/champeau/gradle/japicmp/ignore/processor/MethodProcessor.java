package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiBehavior;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createChangeMethodProvider;
import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createClassProvider;
import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createRemoveMethodProvider;

public class MethodProcessor {
  private final EntityManager manager;
  private final ClassMutator classMutator;

  public MethodProcessor(ClassMutator classMutator, EntityManager manager) {
    this.manager = manager;
    this.classMutator = classMutator;
  }

  public void process(List<JApiMethod> methods, VersionsRange versions) {
    Matcher<JApiMethod, JApiCompatibilityChange> matcher = new Matcher<>(JApiBehavior::getName);
    for (JApiMethod method : methods) {
      List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>(method.getCompatibilityChanges());
      if (compatibilityChanges.isEmpty()) continue;

      for (JApiCompatibilityChange compatibilityChange : compatibilityChanges) {
        switch (compatibilityChange) {
          case METHOD_REMOVED:
          case METHOD_RETURN_TYPE_CHANGED:
          case METHOD_ADDED_TO_PUBLIC_CLASS:
            matcher.add(method, compatibilityChange);
            break;
        }
      }
    }

    doProcess(matcher, versions);
  }

  private void doProcess(Matcher<JApiMethod, JApiCompatibilityChange> matcher, VersionsRange versions) {
    for (Map.Entry<JApiMethod, JApiMethod> entry : matcher.getMatches().entrySet()) {
      JApiMethod key = entry.getKey();
      JApiMethod value = entry.getValue();
      if (manager.matches(createChangeMethodProvider(key, value), versions)) {
        classMutator.removeCompatibilityChange(key, matcher.getReason(key));
        classMutator.removeCompatibilityChange(value, matcher.getReason(value));
      }
    }

    for (JApiMethod unmatchedChange : matcher.getUnmatchedChanges()) {
      JApiCompatibilityChange reason = matcher.getReason(unmatchedChange);
      switch (reason) {
        case METHOD_REMOVED:
          if (manager.matches(createClassProvider(unmatchedChange.getjApiClass()), versions) ||
              manager.matches(createRemoveMethodProvider(unmatchedChange), versions)) {
            classMutator.removeCompatibilityChange(unmatchedChange, reason);
          }
          break;
        case METHOD_RETURN_TYPE_CHANGED:
          if (manager.matches(createChangeMethodProvider(unmatchedChange, unmatchedChange), versions)) {
            classMutator.removeCompatibilityChange(unmatchedChange, reason);
          }
      }

    }
  }
}
