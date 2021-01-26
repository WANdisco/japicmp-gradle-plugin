package me.champeau.gradle.japicmp.archive;

import japicmp.cmp.JarArchiveComparator;
import japicmp.model.JApiClass;
import me.champeau.gradle.japicmp.ignore.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Diff {
  private final List<Info> diffs = new ArrayList<>();
  private final Set<String> archivesName;

  public Diff(JarArchiveComparator comparator, List<Archive> oldArchives, List<Archive> newArchives) {
    archivesName = Stream
        .concat(
            oldArchives.stream().map(Archive::getFileName),
            newArchives.stream().map(Archive::getFileName)
        ).collect(Collectors.toSet());
    for (Archive oldArchive : oldArchives) {
      for (Archive newArchive : newArchives) {
        if (oldArchive.getFileName().equals(newArchive.getFileName())) {
          diffs.add(new Info(comparator, oldArchive, newArchive));
        }
      }
    }
  }

  public boolean containsProject(Project project) {
    return archivesName.contains(project.name);
  }

  public void forEach(Consumer<Info> action) {
    diffs.forEach(action);
  }

  public List<JApiClass> classes() {
    return diffs.stream()
        .map(diffInfo -> diffInfo.classes)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public static class Info {
    private final List<JApiClass> classes;
    private final VersionsRange versionsRange;

    public Info(JarArchiveComparator comparator, Archive oldArchive, Archive newArchive) {
      versionsRange = new VersionsRange(oldArchive.getVersion(), newArchive.getVersion());

      classes = comparator.compare(oldArchive.toJapicmpArchive(), newArchive.toJapicmpArchive());
    }

    public List<JApiClass> getClasses() {
      return classes;
    }

    public VersionsRange getVersionsRange() {
      return versionsRange;
    }
  }
}
