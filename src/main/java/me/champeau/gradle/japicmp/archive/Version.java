package me.champeau.gradle.japicmp.archive;

import java.io.Serializable;
import java.util.Objects;

public class Version implements Comparable<Version>, Serializable {
  private final SemanticVersion version;

  public Version(String version) {
    SemanticVersion v;
    try {
      v = SemanticVersion.semanticVersion(version);
    } catch (VersionParseException e) {
      v = new SemanticVersion(1, 0, null, null);
    }
    this.version = v;
  }

  @Override
  public int compareTo(Version version) {
    return this.version.compareTo(version.version);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Version version1 = (Version) o;
    return Objects.equals(version, version1.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version);
  }

  @Override
  public String toString() {
    return version.toString();
  }

  public static boolean checkIsVersion(String rawVersion) {
    try {
      SemanticVersion.semanticVersion(rawVersion);
    } catch (VersionParseException e) {
      return false;
    }
    return true;
  }

  private static class SemanticVersion implements Comparable<SemanticVersion>, Serializable {
    private final Integer era;
    private final Integer major;
    private final Integer minor;
    private final Integer patch;

    public SemanticVersion(Integer era, Integer major, Integer minor, Integer patch) {
      this.era = era;
      this.major = major;
      this.minor = minor;
      this.patch = patch;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SemanticVersion that = (SemanticVersion) o;
      return Objects.equals(era, that.era)
          && Objects.equals(major, that.major)
          && Objects.equals(minor, that.minor)
          && Objects.equals(patch, that.patch);
    }

    @Override
    public int hashCode() {
      return Objects.hash(era, major, minor, patch);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (era != null) {
        sb.append(era);
      }
      if (major != null) {
        sb.append(".").append(major);
      }
      if (minor != null) {
        sb.append(".").append(minor);
      }
      if (patch != null) {
        sb.append(".").append(patch);
      }
      return sb.toString();
    }

    public static SemanticVersion semanticVersion(String rawVersion) throws VersionParseException {
      if (rawVersion.endsWith(".")) {
        throw new VersionParseException("Incorrect version format with last dot char: " + rawVersion);
      }
      String[] split = rawVersion.split("\\.");

      Integer era;
      try {
         era = Integer.parseInt(split[0]);
      } catch (NumberFormatException e) {
        throw new VersionParseException("Incorrect version format", e);
      }
      Integer major = tryParseInt(split, 1);
      Integer minor = tryParseInt(split, 2);
      Integer patch = tryParseInt(split, 3);

      return new SemanticVersion(era, major, minor, patch);
    }

    public static Integer tryParseInt(String[] arr, int index) {
      try {
        return Integer.parseInt(arr[index]);
      } catch (Exception ignored) {}
      return null;
    }

    @Override
    public int compareTo(SemanticVersion version) {
      if (era == null) return 0;
      int result = Integer.compare(era, version.era);
      if (result != 0) return result;

      if (major == null || version.major == null) return result;
      result = Integer.compare(major, version.major);
      if (result != 0) return result;


      if (minor == null || version.minor == null) return result;
      result = Integer.compare(minor, version.minor);
      if (result != 0) return result;

      if (patch == null || version.patch == null) return result;
      return Integer.compare(patch, version.patch);
    }


  }
}
