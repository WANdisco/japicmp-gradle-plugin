package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiClass;
import japicmp.model.JApiConstructor;
import japicmp.model.JApiField;
import japicmp.model.JApiMethod;

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

  public boolean validateChangeMethod(JApiMethod oldMethod, JApiMethod newMethod) {
    String fullyQualifiedName = oldMethod.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateChangeMethod(oldMethod, newMethod);
    }
    return false;
  }

  public boolean validateRemoveMethod(JApiMethod method) {
    String fullyQualifiedName = method.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveMethod(method);
    }
    return false;
  }

  public boolean validateChangeField(JApiField oldField, JApiField newField) {
    String fullyQualifiedName = oldField.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateChangeField(oldField, newField);
    }
    return false;
  }

  public boolean validateRemoveField(JApiField field) {
    String fullyQualifiedName = field.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveField(field);
    }
    return false;
  }

  public boolean validateChangeConstructor(JApiConstructor oldConstructor, JApiConstructor newConstructor) {
    String fullyQualifiedName = oldConstructor.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateChangeConstructor(oldConstructor, newConstructor);
    }
    return false;
  }

  public boolean validateRemoveConstructor(JApiConstructor constructor) {
    String fullyQualifiedName = constructor.getjApiClass().getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveConstructor(constructor);
    }
    return false;
  }

  public boolean validateRemoveClass(JApiClass clazz) {
    String fullyQualifiedName = clazz.getFullyQualifiedName();
    ClassDescriptor classDescriptor = classEntities.get(fullyQualifiedName);
    if (classDescriptor != null) {
      return classDescriptor.validateRemoveClass(clazz);
    }
    return false;
  }
}
