package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiField;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.List;
import java.util.Map;

public class FieldProcessor {
  private final ClassMutator classMutator;
  private final EntityManager manager;

  public FieldProcessor(ClassMutator classMutator, EntityManager manager) {
    this.classMutator = classMutator;
    this.manager = manager;
  }

  public void process(List<JApiField> fields, VersionsRange versions) {
    Changer<JApiField, JApiChangeStatus> changer = new Changer<>(
        JApiField::getName,
        field -> {
          String targetName = manager.tryToFindMatchChange(field);
          return targetName != null ? targetName : field.getName();
        }
    );
    for (JApiField field : fields) {
      JApiChangeStatus changeStatus = field.getChangeStatus();
      switch (changeStatus) {
        case NEW:
        case REMOVED:
          changer.addToChanges(field, changeStatus);
          break;
        case MODIFIED:
          for (JApiCompatibilityChange compatibilityChange : field.getCompatibilityChanges()) {
            switch (compatibilityChange) {
              case FIELD_TYPE_CHANGED:
                changer.addToChanges(field, JApiChangeStatus.MODIFIED);
            }
          }
          break;
      }
    }

    doProcess(changer, versions);
  }

  private void doProcess(Changer<JApiField, JApiChangeStatus> changer, VersionsRange versions) {
    for (Map.Entry<JApiField, JApiField> entry : changer.getMatches().entrySet()) {
      JApiField key = entry.getKey();
      JApiField value = entry.getValue();
      if (manager.validateChangeField(key, value, versions)) {
        classMutator.removeCompatibilityChange(key, JApiCompatibilityChange.FIELD_REMOVED);
        classMutator.removeCompatibilityChange(value, JApiCompatibilityChange.FIELD_REMOVED);
      }
    }

    for (JApiField unmatchedChange : changer.getUnmatchedChanges()) {
      if (manager.validateRemoveField(unmatchedChange, versions)) {
        classMutator.removeCompatibilityChange(
            unmatchedChange,
            changer.getReason(unmatchedChange) == JApiChangeStatus.REMOVED
                ? JApiCompatibilityChange.FIELD_REMOVED
                : JApiCompatibilityChange.FIELD_TYPE_CHANGED
        );
      }
    }
  }
}
