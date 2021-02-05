package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiImplementedInterface;
import japicmp.model.JApiSuperclass;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class ClassProcessor {
  private final ClassMutator classMutator;
  private final EntityManager manager;

  public ClassProcessor(ClassMutator classMutator, EntityManager manager) {
    this.classMutator = classMutator;
    this.manager = manager;
  }

  public void process(JApiClass clazz, VersionsRange versions) {
    if (manager.matches(ProviderHelper.createClassProvider(clazz), versions)) {
      List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>(clazz.getCompatibilityChanges());
      for (JApiCompatibilityChange compatibilityChange : compatibilityChanges) {
        if (compatibilityChange == JApiCompatibilityChange.CLASS_REMOVED) {
          classMutator.removeCompatibilityChange(clazz, JApiCompatibilityChange.CLASS_REMOVED);
        }
      }
      JApiSuperclass superclass = clazz.getSuperclass();
      if (superclass != null) {
        for (JApiCompatibilityChange compatibilityChange : superclass.getCompatibilityChanges()) {
          if (compatibilityChange == JApiCompatibilityChange.SUPERCLASS_REMOVED) {
            classMutator.removeCompatibilityChange(superclass, JApiCompatibilityChange.SUPERCLASS_REMOVED);
          }
        }
      }
      List<JApiImplementedInterface> interfaces = clazz.getInterfaces();
      if (interfaces != null && !interfaces.isEmpty()) {
        for (JApiImplementedInterface anInterface : interfaces) {
          for (JApiCompatibilityChange compatibilityChange : anInterface.getCompatibilityChanges()) {
            if (compatibilityChange == JApiCompatibilityChange.INTERFACE_REMOVED) {
              classMutator.removeCompatibilityChange(anInterface, JApiCompatibilityChange.INTERFACE_REMOVED);
            }
          }
        }
      }
    }
  }
}
