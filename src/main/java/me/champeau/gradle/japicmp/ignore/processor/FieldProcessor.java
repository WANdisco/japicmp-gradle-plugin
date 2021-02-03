package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiField;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.List;
import java.util.Map;

import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createChangeFieldProvider;
import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createClassProvider;
import static me.champeau.gradle.japicmp.ignore.processor.ProviderHelper.createRemoveFieldProvider;

public class FieldProcessor {
  private final ClassMutator classMutator;
  private final EntityManager manager;

  public FieldProcessor(ClassMutator classMutator, EntityManager manager) {
    this.classMutator = classMutator;
    this.manager = manager;
  }

  public void process(List<JApiField> fields, VersionsRange versions) {
    Matcher<JApiField, JApiChangeStatus> matcher = new Matcher<>(
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
          matcher.add(field, changeStatus);
          break;
        case MODIFIED:
          for (JApiCompatibilityChange compatibilityChange : field.getCompatibilityChanges()) {
            if (compatibilityChange == JApiCompatibilityChange.FIELD_TYPE_CHANGED) {
              matcher.add(field, JApiChangeStatus.MODIFIED);
            }
          }
          break;
      }
    }

    doProcess(matcher, versions);
  }

  private void doProcess(Matcher<JApiField, JApiChangeStatus> matcher, VersionsRange versions) {
    for (Map.Entry<JApiField, JApiField> entry : matcher.getMatches().entrySet()) {
      JApiField key = entry.getKey();
      JApiField value = entry.getValue();
      if (manager.matches(createChangeFieldProvider(key, value), versions)) {
        classMutator.removeCompatibilityChange(key, JApiCompatibilityChange.FIELD_REMOVED);
        classMutator.removeCompatibilityChange(value, JApiCompatibilityChange.FIELD_REMOVED);
      }
    }

    for (JApiField unmatchedChange : matcher.getUnmatchedChanges()) {
      if (manager.matches(createClassProvider(unmatchedChange.getjApiClass()), versions) ||
          manager.matches(createRemoveFieldProvider(unmatchedChange), versions)) {
        classMutator.removeCompatibilityChange(
            unmatchedChange,
            matcher.getReason(unmatchedChange) == JApiChangeStatus.REMOVED
                ? JApiCompatibilityChange.FIELD_REMOVED
                : JApiCompatibilityChange.FIELD_TYPE_CHANGED
        );
      }
    }
  }
}
