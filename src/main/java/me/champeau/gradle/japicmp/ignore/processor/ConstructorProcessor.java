package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiBehavior;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiConstructor;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.List;
import java.util.Map;

public class ConstructorProcessor {
  private final ClassMutator classMutator;
  private final EntityManager manager;

  public ConstructorProcessor(ClassMutator classMutator, EntityManager manager) {
    this.classMutator = classMutator;
    this.manager = manager;
  }

  public void process(List<JApiConstructor> constructors) {
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

    doProcess(changer);
  }

  private void doProcess(Changer<JApiConstructor, JApiChangeStatus> changer) {
    for (Map.Entry<JApiConstructor, JApiConstructor> entry : changer.getMatches().entrySet()) {
      JApiConstructor key = entry.getKey();
      JApiConstructor value = entry.getValue();
      if (manager.validateChangeConstructor(key, value)) {
        classMutator.removeCompatibilityChange(key, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
        classMutator.removeCompatibilityChange(value, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
      }
    }

    for (JApiConstructor unmatchedChange : changer.getUnmatchedChanges()) {
      if (manager.validateRemoveConstructor(unmatchedChange)) {
        classMutator.removeCompatibilityChange(unmatchedChange, JApiCompatibilityChange.CONSTRUCTOR_REMOVED);
      }
    }
  }
}
