package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiBehavior;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiConstructor;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;
import me.champeau.gradle.japicmp.ignore.entity.Provider;

import java.util.List;
import java.util.Map;

public class ConstructorProcessor {
  private final ClassMutator classMutator;
  private final EntityManager manager;
  private final Provider.MutableProvider<JApiConstructor> provider = ProviderHelper.createDefaultConstructorProvider();
  private final Provider.MutableProvider<JApiClass> classProvider = ProviderHelper.createDefaultClassProvider();

  public ConstructorProcessor(ClassMutator classMutator, EntityManager manager) {
    this.classMutator = classMutator;
    this.manager = manager;
  }

  public void process(List<JApiConstructor> constructors, VersionsRange versions) {
    Changer<JApiConstructor, JApiChangeStatus> changer = new Changer<>(JApiBehavior::getName);
    for (JApiConstructor constructor : constructors) {
      JApiChangeStatus changeStatus = constructor.getChangeStatus();
      switch (changeStatus) {
        case NEW:
        case REMOVED:
          changer.addToChanges(constructor, changeStatus);
          break;
      }
    }

    doProcess(changer, versions);
  }

  private void doProcess(Changer<JApiConstructor, JApiChangeStatus> changer, VersionsRange versions) {
    for (Map.Entry<JApiConstructor, JApiConstructor> entry : changer.getMatches().entrySet()) {
      JApiConstructor key = entry.getKey();
      JApiConstructor value = entry.getValue();
      provider.setChangeElement(key, value);
      if (manager.validate(provider, versions)) {
        classMutator.removeCompatibilityChange(key, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
        classMutator.removeCompatibilityChange(value, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
      }
    }

    for (JApiConstructor unmatchedChange : changer.getUnmatchedChanges()) {
      classProvider.setRemoveElement(unmatchedChange.getjApiClass());
      provider.setRemoveElement(unmatchedChange);
      if (manager.validate(classProvider, versions) || manager.validate(provider, versions)) {
        classMutator.removeCompatibilityChange(unmatchedChange, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
      }
    }
  }
}
