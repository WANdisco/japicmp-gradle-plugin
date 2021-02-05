package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiField;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {
  private final Map<String, Entity<?>> entities = new HashMap<>();

  public EntityManager(List<Entity<?>> entities) {
    for (Entity<?> entity : entities) {
      this.entities.put(entity.getIdentifier(), entity);
    }
  }

  public boolean matches(Provider provider, VersionsRange versionsRange) {
    Entity<?> entity = entities.get(provider.getIdentifier());
    if (entity != null) {

      return versionsRange.isAffectVersion(entity.getVersion())
          && entity.process(provider);
    }
    return false;
  }

  public String tryToFindMatchChange(JApiField field) {
    for (Entity<?> value : entities.values()) {
      if (value instanceof ChangeEntity) {
        Element<?> oldElement = ((ChangeEntity<?>) value).getOldElement();
        Element<?> newElement = ((ChangeEntity<?>) value).getNewElement();
        if (oldElement instanceof FieldElement && newElement instanceof FieldElement) {
          String oldFieldName = ((FieldElement) oldElement).getFieldName();
          String newFieldName = ((FieldElement) newElement).getFieldName();
          if (oldFieldName.equals(field.getName())) {
            return newFieldName;
          } else if (newFieldName.equals(field.getName())) {
            return oldFieldName;
          }
        }
      }
    }
    return null;
  }
}
