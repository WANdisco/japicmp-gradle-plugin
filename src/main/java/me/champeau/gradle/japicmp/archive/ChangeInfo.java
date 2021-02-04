package me.champeau.gradle.japicmp.archive;

import japicmp.cmp.JarArchiveComparator;
import japicmp.model.JApiClass;

import java.util.List;

public class ChangeInfo {
  private final VersionsRange versionsRange;
  private final JarArchiveComparator comparator;
  private final Archive oldArchive;
  private final Archive newArchive;

  public ChangeInfo(JarArchiveComparator comparator, Archive oldArchive, Archive newArchive) {
    this.comparator = comparator;
    this.oldArchive = oldArchive;
    this.newArchive = newArchive;
    versionsRange = new VersionsRange(oldArchive.getVersion(), newArchive.getVersion());
  }

  public List<JApiClass> classes() {
    return comparator.compare(oldArchive.toJapicmpArchive(), newArchive.toJapicmpArchive());
  }

  public boolean isCorrectName(String name) {
    return oldArchive.getFileName().equals(name) || newArchive.getFileName().equals(name);
  }

  public VersionsRange getVersionsRange() {
    return versionsRange;
  }
}
