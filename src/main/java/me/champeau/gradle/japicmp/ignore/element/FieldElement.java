package me.champeau.gradle.japicmp.ignore.element;

import japicmp.model.JApiField;

import java.util.Objects;

public class FieldElement extends Element<JApiField> {
  private final String fieldType;
  private final String fieldName;

  public FieldElement(String fullPackageName, String className, String fieldType, String fieldName) {
    super(fullPackageName, className);
    this.fieldType = fieldType;
    this.fieldName = fieldName;
  }

  public String getFieldType() {
    return fieldType;
  }

  public String getFieldName() {
    return fieldName;
  }

  @Override
  protected String doGetIdentifier() {
    return fieldName + ":" + fieldType;
  }

  @Override
  public boolean process(JApiField field) {
    if (!validateName(field)) return false;
    if (!validateType(field)) return false;

    return true;
  }

  private boolean validateName(JApiField field) {
    return Objects.equals(fieldName, field.getName());
  }

  private boolean validateType(JApiField field) {
    return Objects.equals(fieldType, field.getType().getNewValue())
        || Objects.equals(fieldType, field.getType().getOldValue());
  }

  public static String extractIdentifier(JApiField field, boolean isOld) {
    return field.getjApiClass().getFullyQualifiedName()
        + ":" + field.getName()
        + ":" + (isOld ? field.getType().getOldValue() : field.getType().getNewValue());
  }
}
