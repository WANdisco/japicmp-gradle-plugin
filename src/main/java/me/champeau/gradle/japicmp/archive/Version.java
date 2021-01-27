package me.champeau.gradle.japicmp.archive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
      StringBuilder sb = new StringBuilder();
      for (int element : elements) {
        sb.append(element).append(".");
      }
      if (elements.length > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      return sb.toString();
    }

    public static SemanticVersion semanticVersion(String rawVersion) throws VersionParseException {
      if (rawVersion.endsWith(".")) {
        throw new VersionParseException("Incorrect version format with last dot char: " + rawVersion);
      }
      String[] split = rawVersion.split("\\.");

      List<Integer> result = new ArrayList<>();
      for (String s : split) {
        try {
          int i = Integer.parseInt(s);
          result.add(i);
        } catch (Exception e) {
          break;
        }
      }

      if (result.isEmpty()) {
        throw new VersionParseException("The input: " + rawVersion + " can't be parse as version");
      }

      return new SemanticVersion(result.stream().mapToInt(i->i).toArray());
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
