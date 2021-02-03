package me.champeau.gradle.japicmp.archive;

import java.io.Serializable;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Version implements Comparable<Version>, Serializable {
  private final int[] elements;

  public Version(String version) {
    int[] elements;
    try {
      elements = Version.parse(version);
    } catch (VersionParseException e) {
      elements = new int[] {1, 0};
    }
    this.elements = elements;
  }

  private static OptionalInt parseSection(String section) {
    try {
      return OptionalInt.of(Integer.parseInt(section));
    } catch (Exception e) {
      return OptionalInt.empty();
    }
  }

  private static int[] parse(String rawVersion) throws VersionParseException {
    if (rawVersion.endsWith(".")) {
      throw new VersionParseException("Incorrect version format with last dot char: " + rawVersion);
    }
    int[] versionSections = Arrays.stream(rawVersion.split("\\."))
        .map(Version::parseSection)
        .filter(OptionalInt::isPresent)
        .mapToInt(OptionalInt::getAsInt)
        .toArray();
    if (versionSections.length == 0) {
      throw new VersionParseException("The input: " + rawVersion + " can't be parse as version");
    }
    return versionSections;
  }

  @Override
  public int compareTo(Version version) {
    int minLength = Math.min(elements.length, version.elements.length);
    for (int i = 0; i < minLength; i++) {
      int compare = Integer.compare(elements[i], version.elements[i]);
      if (compare != 0) {
        return compare;
      }
    }
    return Integer.compare(elements.length, version.elements.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Version that = (Version) o;
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

  public static boolean checkIsVersion(String rawVersion) {
    try {
      parse(rawVersion);
    } catch (VersionParseException e) {
      return false;
    }
    return true;
  }
}
