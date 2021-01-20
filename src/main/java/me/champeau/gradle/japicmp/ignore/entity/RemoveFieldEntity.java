package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiField;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;

public class RemoveFieldEntity extends Entity {
  private final FieldElement entity;

  public RemoveFieldEntity(String version, FieldElement entity) {
    super(version);
    this.entity = entity;
  }

  public boolean validate(JApiField field) {
    return entity.process(field);
  }

  public String getName() {
    return entity.getFieldName();
  }

  @Override
  public String getClassName() {
    return entity.getCanonicalName();
  }
}
