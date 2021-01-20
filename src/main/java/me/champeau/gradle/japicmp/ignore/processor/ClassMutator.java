package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibility;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiConstructor;
import japicmp.model.JApiField;
import japicmp.model.JApiImplementedInterface;
import japicmp.model.JApiMethod;
import japicmp.model.JApiSuperclass;

import java.lang.reflect.Field;
import java.util.List;

public class ClassMutator {

  public void removeCompatibilityChange(JApiMethod method, JApiCompatibilityChange compatibilityChange) {
    List<JApiCompatibilityChange> compatibilityChanges = method.getCompatibilityChanges();
    compatibilityChanges.remove(compatibilityChange);
    if (compatibilityChanges.isEmpty()) {
      try {
        Field changeStatus = method.getClass().getSuperclass().getDeclaredField("changeStatus");
        changeStatus.setAccessible(true);
        changeStatus.set(method, JApiChangeStatus.UNCHANGED);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public void removeCompatibilityChange(JApiField field, JApiCompatibilityChange compatibilityChange) {
    List<JApiCompatibilityChange> compatibilityChanges = field.getCompatibilityChanges();
    compatibilityChanges.remove(compatibilityChange);
    if (compatibilityChanges.isEmpty()) {
      try {
        Field changeStatus = field.getClass().getDeclaredField("changeStatus");
        changeStatus.setAccessible(true);
        changeStatus.set(field, JApiChangeStatus.UNCHANGED);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public void removeCompatibilityChange(JApiConstructor constructor, JApiCompatibilityChange compatibilityChange) {
    List<JApiCompatibilityChange> compatibilityChanges = constructor.getCompatibilityChanges();
    compatibilityChanges.remove(compatibilityChange);
    if (compatibilityChanges.isEmpty()) {
      try {
        Field changeStatus = constructor.getClass().getSuperclass().getDeclaredField("changeStatus");
        changeStatus.setAccessible(true);
        changeStatus.set(constructor, JApiChangeStatus.UNCHANGED);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public void removeCompatibilityChange(JApiClass clazz, JApiCompatibilityChange compatibilityChange) {
    List<JApiCompatibilityChange> compatibilityChanges = clazz.getCompatibilityChanges();
    compatibilityChanges.remove(compatibilityChange);
    tryClearClass(clazz);
  }

  public void removeCompatibilityChange(JApiSuperclass superclass, JApiCompatibilityChange compatibilityChange) {
    List<JApiCompatibilityChange> compatibilityChanges = superclass.getCompatibilityChanges();
    compatibilityChanges.remove(compatibilityChange);
    if (compatibilityChanges.isEmpty()) {
      try {
        Field changeStatus = superclass.getClass().getDeclaredField("changeStatus");
        changeStatus.setAccessible(true);
        changeStatus.set(superclass, JApiChangeStatus.UNCHANGED);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public void removeCompatibilityChange(JApiImplementedInterface anInterface, JApiCompatibilityChange compatibilityChange) {
    List<JApiCompatibilityChange> compatibilityChanges = anInterface.getCompatibilityChanges();
    compatibilityChanges.remove(compatibilityChange);
    if (compatibilityChanges.isEmpty()) {
      try {
        Field changeStatus = anInterface.getClass().getDeclaredField("changeStatus");
        changeStatus.setAccessible(true);
        changeStatus.set(anInterface, JApiChangeStatus.UNCHANGED);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public void tryClearClass(JApiClass clazz) {
    if (clazz.getCompatibilityChanges().isEmpty()
        && !hasChanges(clazz.getMethods())
        && !hasChanges(clazz.getFields())
        && !hasChanges(clazz.getConstructors())) {
      try {
        Field changeStatus = clazz.getClass().getDeclaredField("changeStatus");
        changeStatus.setAccessible(true);
        changeStatus.set(clazz, JApiChangeStatus.UNCHANGED);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean hasChanges(List<? extends JApiCompatibility> list) {
    for (JApiCompatibility jApiCompatibility : list) {
      if (!jApiCompatibility.getCompatibilityChanges().isEmpty()) return true;
    }
    return false;
  }


}
