package me.champeau.gradle.japicmp.archive;

public class VersionParseException extends Exception {
  public VersionParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public VersionParseException(String message) {
    super(message);
  }
}
