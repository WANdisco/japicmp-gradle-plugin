package me.champeau.gradle.japicmp.archive;

import japicmp.cmp.JarArchiveComparator;
import japicmp.model.JApiClass;
import me.champeau.gradle.japicmp.ignore.CompatibilityChangesFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Diff {
  private final List<ChangeInfo> diffs = new ArrayList<>();

  public Diff(JarArchiveComparator comparator, List<Archive> oldArchives, List<Archive> newArchives) {
    for (Archive oldArchive : oldArchives) {
      for (Archive newArchive : newArchives) {
        if (oldArchive.getFileName().equals(newArchive.getFileName())) {
          diffs.add(new ChangeInfo(comparator, oldArchive, newArchive));
        }
      }
    }
  }

  public List<JApiClass> classes() {
    return diffs.stream()
        .map(ChangeInfo::classes)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public List<JApiClass> classes(CompatibilityChangesFilter filter) {
    return diffs.stream()
        .map(filter::filterChanges)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

}
