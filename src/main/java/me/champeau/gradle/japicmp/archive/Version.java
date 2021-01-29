package me.champeau.gradle.japicmp.archive;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Version implements Comparable<Version>, Serializable {
  private final SemanticVersion version;

  public Version(String version) {
    SemanticVersion v;
    try {
      v = SemanticVersion.semanticVersion(version);
    } catch (VersionParseException e) {
      v = new SemanticVersion(new int[] {1, 0});
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
    private final int[] elements;

    public SemanticVersion(int[] elements) {
      this.elements = elements;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SemanticVersion that = (SemanticVersion) o;
      return Arrays.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
      return Arrays.stream(elements)
          .mapToObj(String::valueOf)
          .collect(Collectors.joining("."));
    }

    private static OptionalInt parseSection(String section) {
      try {
        return OptionalInt.of(Integer.parseInt(section));
      } catch (Exception e) {
        return OptionalInt.empty();
      }
    }

    public static SemanticVersion semanticVersion(String rawVersion) throws VersionParseException {
      if (rawVersion.endsWith(".")) {
        throw new VersionParseException("Incorrect version format with last dot char: " + rawVersion);
      }
      int[] versionSections = Arrays.stream(rawVersion.split("\\."))
          .map(SemanticVersion::parseSection)
          .filter(OptionalInt::isPresent)
          .mapToInt(OptionalInt::getAsInt)
          .toArray();
      if (versionSections.length == 0) {
        throw new VersionParseException("The input: " + rawVersion + " can't be parse as version");
      }
      return new SemanticVersion(versionSections);
    }

    @Override
    public int compareTo(SemanticVersion version) {
      int minLength = Math.min(elements.length, version.elements.length);
      for (int i = 0; i < minLength; i++) {
        int compare = Integer.compare(elements[i], version.elements[i]);
        if (compare != 0) {
          return compare;
        }
      }
      return Integer.compare(elements.length, version.elements.length);
    }


  }
}
