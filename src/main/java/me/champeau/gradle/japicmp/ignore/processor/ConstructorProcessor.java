package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiBehavior;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiConstructor;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.List;
import java.util.Map;

import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createChangeConstructorProvider;
import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createClassProvider;
import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createRemoveConstructorProvider;

public class ConstructorProcessor {
  private final ClassMutator classMutator;
  private final EntityManager manager;

  public ConstructorProcessor(ClassMutator classMutator, EntityManager manager) {
    this.classMutator = classMutator;
    this.manager = manager;
  }

  public void process(List<JApiConstructor> constructors, VersionsRange versions) {
    Matcher<JApiConstructor, JApiChangeStatus> matcher = new Matcher<>(JApiBehavior::getName);
    for (JApiConstructor constructor : constructors) {
      JApiChangeStatus changeStatus = constructor.getChangeStatus();
      switch (changeStatus) {
        case NEW:
        case REMOVED:
          matcher.add(constructor, changeStatus);
          break;
      }
    }

    doProcess(matcher, versions);
  }

  private void doProcess(Matcher<JApiConstructor, JApiChangeStatus> matcher, VersionsRange versions) {
    for (Map.Entry<JApiConstructor, JApiConstructor> entry : matcher.getMatches().entrySet()) {
      JApiConstructor key = entry.getKey();
      JApiConstructor value = entry.getValue();
      if (manager.matches(createChangeConstructorProvider(key, value), versions)) {
        classMutator.removeCompatibilityChange(key, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
        classMutator.removeCompatibilityChange(value, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
      }
    }

    for (JApiConstructor unmatchedChange : matcher.getUnmatchedChanges()) {
      if (manager.matches(createClassProvider(unmatchedChange.getjApiClass()), versions) ||
          manager.matches(createRemoveConstructorProvider(unmatchedChange), versions)) {
        classMutator.removeCompatibilityChange(unmatchedChange, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
      }
    }
  }
}
