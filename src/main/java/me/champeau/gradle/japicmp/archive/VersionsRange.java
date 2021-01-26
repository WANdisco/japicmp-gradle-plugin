package me.champeau.gradle.japicmp.archive;

public class VersionsRange {
  private final Version oldVersion;
  private final Version newVersion;

  public VersionsRange(Version oldVersion, Version newVersion) {
    this.oldVersion = oldVersion;
    this.newVersion = newVersion;
  }

  public boolean isAffectVersion(Version version) {
    return version.compareTo(oldVersion) >= 0 && version.compareTo(newVersion) <= 0;
  }
}
