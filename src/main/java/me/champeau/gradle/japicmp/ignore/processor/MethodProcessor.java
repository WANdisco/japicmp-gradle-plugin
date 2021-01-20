package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiBehavior;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodProcessor {
  private final EntityManager manager;
  private final ClassMutator classMutator;

  public MethodProcessor(ClassMutator classMutator, EntityManager manager) {
    this.manager = manager;
    this.classMutator = classMutator;
  }

  public void process(List<JApiMethod> methods) {
    Changer<JApiMethod, JApiCompatibilityChange> changer = new Changer<>(JApiBehavior::getName);
    for (JApiMethod method : methods) {
      List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>(method.getCompatibilityChanges());
      if (compatibilityChanges.isEmpty()) continue;

      for (JApiCompatibilityChange compatibilityChange : compatibilityChanges) {
        switch (compatibilityChange) {
          case METHOD_REMOVED:
          case METHOD_RETURN_TYPE_CHANGED:
          case METHOD_ADDED_TO_PUBLIC_CLASS:
            changer.addToChanges(method, compatibilityChange);
            break;
        }
      }
    }

    doProcess(changer);
  }

  private void doProcess(Changer<JApiMethod, JApiCompatibilityChange> changer) {
    for (Map.Entry<JApiMethod, JApiMethod> entry : changer.getMatches().entrySet()) {
      JApiMethod key = entry.getKey();
      JApiMethod value = entry.getValue();
      if (manager.validateChangeMethod(key, value)) {
        classMutator.removeCompatibilityChange(key, changer.getReason(key));
        classMutator.removeCompatibilityChange(value, changer.getReason(value));
      }
    }

    for (JApiMethod unmatchedChange : changer.getUnmatchedChanges()) {
      JApiCompatibilityChange reason = changer.getReason(unmatchedChange);
      switch (reason) {
        case METHOD_REMOVED:
          if (manager.validateRemoveMethod(unmatchedChange)) {
            classMutator.removeCompatibilityChange(unmatchedChange, reason);
          }
          break;
        case METHOD_RETURN_TYPE_CHANGED:
          if (manager.validateChangeMethod(unmatchedChange, unmatchedChange)) {
            classMutator.removeCompatibilityChange(unmatchedChange, reason);
          }
      }

    }
  }
}
