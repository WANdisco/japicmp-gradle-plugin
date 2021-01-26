package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiClass;
import japicmp.model.JApiConstructor;
import japicmp.model.JApiField;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.archive.VersionsRange;

import java.util.HashMap;
import java.util.Map;

public class ClassDescriptor {
  private final String fullName;

  private final Map<String, RemoveMethodEntity> removedMethods = new HashMap<>();
  private final Map<String, ChangeMethodEntity> changedMethods = new HashMap<>();

  private final Map<String, RemoveFieldEntity> removedFields = new HashMap<>();
  private final Map<String, ChangeFieldEntity> changedFields = new HashMap<>();

  private final Map<String, RemoveConstructorEntity> removedConstructors = new HashMap<>();
  private final Map<String, ChangeConstructorEntity> changedConstructors = new HashMap<>();

  private final Map<String, RemoveClassEntity> removeClasses = new HashMap<>();


  public ClassDescriptor(String packageClassName) {
    this.fullName = packageClassName;
  }

  public void add(Entity entity) {
    if (entity instanceof RemoveMethodEntity) {
      RemoveMethodEntity castedEntity = (RemoveMethodEntity) entity;
      removedMethods.put((castedEntity).getName(), castedEntity);
    } else if (entity instanceof ChangeMethodEntity) {
      ChangeMethodEntity castedEntity = (ChangeMethodEntity) entity;
      changedMethods.put(castedEntity.getName(), castedEntity);
    } else if (entity instanceof RemoveFieldEntity) {
      RemoveFieldEntity castedEntity = (RemoveFieldEntity) entity;
      removedFields.put((castedEntity).getName(), castedEntity);
    } else if (entity instanceof ChangeFieldEntity) {
      ChangeFieldEntity castedEntity = (ChangeFieldEntity) entity;
      changedFields.put((castedEntity).getFirstFieldName(), castedEntity);
      changedFields.put((castedEntity).getSecondFieldName(), castedEntity);
    } else if (entity instanceof RemoveConstructorEntity) {
      RemoveConstructorEntity castedEntity = (RemoveConstructorEntity) entity;
      removedConstructors.put((castedEntity).getName(), castedEntity);
    } else if (entity instanceof ChangeConstructorEntity) {
      ChangeConstructorEntity castedEntity = (ChangeConstructorEntity) entity;
      changedConstructors.put((castedEntity).getName(), castedEntity);
    } else if (entity instanceof RemoveClassEntity) {
      RemoveClassEntity castedEntity = (RemoveClassEntity) entity;
      removeClasses.put(castedEntity.getClassName(), castedEntity);
    }
  }

  public String getName() {
    return fullName;
  }

  public String tryToFindCompareField(JApiField field) {
    String fieldName = field.getName();
    ChangeFieldEntity changeFieldEntity = changedFields.get(fieldName);
    if (changeFieldEntity != null) {
      return changeFieldEntity.mapName(fieldName);
    }
    return null;
  }

  public boolean validateChangeMethod(JApiMethod oldMethod, JApiMethod newMethod, VersionsRange versions) {
    ChangeMethodEntity changeMethodEntity = changedMethods.get(oldMethod.getName());
    if (changeMethodEntity == null) return false;

    return versions.isAffectVersion(changeMethodEntity.getVersion()) && changeMethodEntity.validate(oldMethod, newMethod);
  }

  public boolean validateRemoveMethod(JApiMethod method, VersionsRange versions) {
    if (validateRemoveClass(method.getjApiClass(), versions)) {
      return true;
    }

    RemoveMethodEntity removeMethodEntity = removedMethods.get(method.getName());
    if (removeMethodEntity == null) return false;

    return versions.isAffectVersion(removeMethodEntity.getVersion()) && removeMethodEntity.validate(method);
  }

  public boolean validateChangeField(JApiField oldField, JApiField newField, VersionsRange versions) {
    ChangeFieldEntity changeFieldEntity = changedFields.get(oldField.getName());
    if (changeFieldEntity == null) return false;

    return versions.isAffectVersion(changeFieldEntity.getVersion()) && changeFieldEntity.validate(oldField, newField);
  }

  public boolean validateRemoveField(JApiField field, VersionsRange versions) {
    if (validateRemoveClass(field.getjApiClass(), versions)) {
      return true;
    }
    RemoveFieldEntity removeFieldEntity = removedFields.get(field.getName());
    if (removeFieldEntity == null) return false;

    return versions.isAffectVersion(removeFieldEntity.getVersion()) && removeFieldEntity.validate(field);
  }

  public boolean validateChangeConstructor(JApiConstructor oldConstructor, JApiConstructor newConstructor, VersionsRange versions) {
    ChangeConstructorEntity changeConstructorEntity = changedConstructors.get(oldConstructor.getName());
    if (changeConstructorEntity == null) return false;

    return versions.isAffectVersion(changeConstructorEntity.getVersion()) && changeConstructorEntity.validate(oldConstructor, newConstructor);
  }

  public boolean validateRemoveConstructor(JApiConstructor constructor, VersionsRange versions) {
    if (validateRemoveClass(constructor.getjApiClass(), versions)) {
      return true;
    }

    RemoveConstructorEntity removeConstructorEntity = removedConstructors.get(constructor.getName());
    if (removeConstructorEntity == null) return false;

    return versions.isAffectVersion(removeConstructorEntity.getVersion()) && removeConstructorEntity.validate(constructor);
  }

  public boolean validateRemoveClass(JApiClass clazz, VersionsRange versions) {
    RemoveClassEntity removeClassEntity = removeClasses.get(clazz.getFullyQualifiedName());
    if (removeClassEntity == null) return false;

    return versions.isAffectVersion(removeClassEntity.getVersion()) && removeClassEntity.validate(clazz);
  }
}
