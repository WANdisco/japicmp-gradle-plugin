package me.champeau.gradle.japicmp.archive;

import japicmp.cmp.JApiCmpArchive;

import java.io.File;
import java.io.Serializable;

public class Archive implements Serializable {
  private final File file;
  private final Version version;

  public Archive(File file, String version) {
    this.file = file;
    this.version = new Version(version);
  }

  public File getFile() {
    return file;
  }

  public Version getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return file.getName();
  }
  
  
  public String getFileName() {
    String name = file.getName();
    try {
      int i = name.lastIndexOf(version.toString());
      return name.substring(0, i - 1);
    } catch (Exception ignored) {}
    return name;
  }

  public JApiCmpArchive toJapicmpArchive() {
    return new JApiCmpArchive(file, version.toString());
  }
}
