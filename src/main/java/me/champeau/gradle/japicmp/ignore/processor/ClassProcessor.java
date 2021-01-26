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
  private final MethodProcessor methodProcessor;
  private final FieldProcessor fieldProcessor;
  private final ConstructorProcessor constructorProcessor;
  private final EntityManager manager;

  public ClassProcessor(EntityManager manager) {
    this.manager = manager;
    classMutator = new ClassMutator();
    methodProcessor = new MethodProcessor(classMutator, manager);
    fieldProcessor = new FieldProcessor(classMutator, manager);
    constructorProcessor = new ConstructorProcessor(classMutator, manager);
  }

  public void process(List<JApiClass> classes, VersionsRange versions) {
    for (JApiClass clazz : classes) {
      doProcessClass(clazz, versions);
      methodProcessor.process(clazz.getMethods(), versions);
      fieldProcessor.process(clazz.getFields(), versions);
      constructorProcessor.process(clazz.getConstructors(), versions);
      classMutator.tryClearClass(clazz);
    }
  }

  private void doProcessClass(JApiClass clazz, VersionsRange versions) {
    if (manager.validateRemoveClass(clazz, versions)) {
      List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>(clazz.getCompatibilityChanges());
      for (JApiCompatibilityChange compatibilityChange : compatibilityChanges) {
        switch (compatibilityChange) {
          case CLASS_REMOVED:
            classMutator.removeCompatibilityChange(clazz, JApiCompatibilityChange.CLASS_REMOVED);
        }
      }
      JApiSuperclass superclass = clazz.getSuperclass();
      if (superclass != null) {
        for (JApiCompatibilityChange compatibilityChange : superclass.getCompatibilityChanges()) {
          switch (compatibilityChange) {
            case SUPERCLASS_REMOVED:
              classMutator.removeCompatibilityChange(superclass, JApiCompatibilityChange.SUPERCLASS_REMOVED);
          }
        }
      }
      List<JApiImplementedInterface> interfaces = clazz.getInterfaces();
      if (interfaces != null && !interfaces.isEmpty()) {
        for (JApiImplementedInterface anInterface : interfaces) {
          for (JApiCompatibilityChange compatibilityChange : anInterface.getCompatibilityChanges()) {
            switch (compatibilityChange) {
              case INTERFACE_REMOVED:
                classMutator.removeCompatibilityChange(anInterface, JApiCompatibilityChange.INTERFACE_REMOVED);
            }
          }
        }
      }
    }
  }
}
