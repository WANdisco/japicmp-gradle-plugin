package me.champeau.gradle.japicmp;

import japicmp.cmp.JApiCmpArchive;
import me.champeau.gradle.japicmp.ignore.Parser;

import java.io.File;
import java.io.Serializable;

public class Archive implements Serializable {
  private final File file;
  private final String version;

  public Archive(File file, String version) {
    this.file = file;
    this.version = version;
  }

  public File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return file.getName();
  }
  
  
  public String getFileName() {
    String name = file.getName();
    try {
      String[] nameAndExtension = Parser.splitByLastDotChar(name);
      String s = nameAndExtension[0];
      int i = s.lastIndexOf(version);
      return s.substring(0, i - 1);
    } catch (Exception ignored) {}
    return name;
  }

  public JApiCmpArchive toJapicmpArchive() {
    return new JApiCmpArchive(file, version);
  }
}
