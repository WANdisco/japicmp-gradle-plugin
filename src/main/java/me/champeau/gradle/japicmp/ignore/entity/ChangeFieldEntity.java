package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiField;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;


public class ChangeFieldEntity extends Entity {
  private final FieldElement prevValue;
  private final FieldElement newValue;

  public ChangeFieldEntity(String version, FieldElement prevValue, FieldElement newValue) {
    super(version);
    this.prevValue = prevValue;
    this.newValue = newValue;
  }

  public boolean validate(JApiField oldField, JApiField newField) {
    if (prevValue.getClass() != newValue.getClass()) return false;

    boolean prevValidate = prevValue.process(oldField);
    boolean newValidate = newValue.process(newField);

    return prevValidate && newValidate;
  }

  public String getFirstFieldName() {
    return prevValue.getFieldName();
  }

  public String getSecondFieldName() {
    return newValue.getFieldName();
  }

  public String mapName(String name) {
    if (prevValue.getFieldName().equals(name)) {
      return newValue.getFieldName();
    } else if (newValue.getFieldName().equals(name)) {
      return prevValue.getFieldName();
    }
    return null;
  }

  @Override
  public String getClassName() {
    return prevValue.getCanonicalName();
  }
}
