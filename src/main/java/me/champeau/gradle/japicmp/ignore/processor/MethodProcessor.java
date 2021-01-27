package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiBehavior;
import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;
import me.champeau.gradle.japicmp.ignore.entity.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodProcessor {
  private final EntityManager manager;
  private final ClassMutator classMutator;

  private final Provider.MutableProvider<JApiMethod> mutableProvider = ProviderHelper.createDefaultMethodProvider();
  private final Provider.MutableProvider<JApiClass> classProvider = ProviderHelper.createDefaultClassProvider();

  public MethodProcessor(ClassMutator classMutator, EntityManager manager) {
    this.manager = manager;
    this.classMutator = classMutator;
  }

  public void process(List<JApiMethod> methods, VersionsRange versions) {
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

    doProcess(changer, versions);
  }

  private void doProcess(Changer<JApiMethod, JApiCompatibilityChange> changer, VersionsRange versions) {
    for (Map.Entry<JApiMethod, JApiMethod> entry : changer.getMatches().entrySet()) {
      JApiMethod key = entry.getKey();
      JApiMethod value = entry.getValue();
      mutableProvider.setChangeElement(key, value);
      if (manager.validate(mutableProvider, versions)) {
        classMutator.removeCompatibilityChange(key, changer.getReason(key));
        classMutator.removeCompatibilityChange(value, changer.getReason(value));
      }
    }

    for (JApiMethod unmatchedChange : changer.getUnmatchedChanges()) {
      JApiCompatibilityChange reason = changer.getReason(unmatchedChange);
      switch (reason) {
        case METHOD_REMOVED:
          classProvider.setRemoveElement(unmatchedChange.getjApiClass());
          if (manager.validate(classProvider, versions) || manager.validate(mutableProvider, versions)) {
            classMutator.removeCompatibilityChange(unmatchedChange, reason);
          }
          break;
        case METHOD_RETURN_TYPE_CHANGED:
          mutableProvider.setChangeElement(unmatchedChange, unmatchedChange);
          if (manager.validate(mutableProvider, versions)) {
            classMutator.removeCompatibilityChange(unmatchedChange, reason);
          }
      }

    }
  }
}
