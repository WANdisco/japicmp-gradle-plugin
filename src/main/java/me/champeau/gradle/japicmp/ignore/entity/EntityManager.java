package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiClass;
import japicmp.model.JApiConstructor;
import japicmp.model.JApiField;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.archive.VersionsRange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {
  private final Map<String, ClassDescriptor> classEntities = new HashMap<>();

  public EntityManager(List<ClassDescriptor> entities) {
    for (ClassDescriptor classDescriptor : entities) {
      classEntities.put(classDescriptor.getName(), classDescriptor);
    }
  }

  public String tryToFindMatchChange(JApiField field) {
    ClassDescriptor classDescriptor = classEntities.get(field.getjApiClass().getFullyQualifiedName());
    if (classDescriptor != null) {
      return classDescriptor.tryToFindCompareField(field);
    }
    return null;
  }

  public boolean validateChangeMethod(JApiMethod oldMethod, JApiMethod newMethod, VersionsRange versions) {
    String fullyQualifiedName = oldMethod.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateChangeMethod(oldMethod, newMethod, versions);
    }
    return false;
  }

  public boolean validateRemoveMethod(JApiMethod method, VersionsRange versions) {
    String fullyQualifiedName = method.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveMethod(method, versions);
    }
    return false;
  }

  public boolean validateChangeField(JApiField oldField, JApiField newField, VersionsRange versions) {
    String fullyQualifiedName = oldField.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateChangeField(oldField, newField, versions);
    }
    return false;
  }

  public boolean validateRemoveField(JApiField field, VersionsRange versions) {
    String fullyQualifiedName = field.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveField(field, versions);
    }
    return false;
  }

  public boolean validateChangeConstructor(JApiConstructor oldConstructor, JApiConstructor newConstructor, VersionsRange versions) {
    String fullyQualifiedName = oldConstructor.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateChangeConstructor(oldConstructor, newConstructor, versions);
    }
    return false;
  }

  public boolean validateRemoveConstructor(JApiConstructor constructor, VersionsRange versions) {
    String fullyQualifiedName = constructor.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveConstructor(constructor, versions);
    }
    return false;
  }

  public boolean validateRemoveClass(JApiClass clazz, VersionsRange versions) {
    String fullyQualifiedName = clazz.getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveClass(clazz, versions);
    }
    return false;
  }
}
